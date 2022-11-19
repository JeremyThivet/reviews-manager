import React, {useState, useContext, useEffect, useRef} from 'react';
import { useParams, useNavigate } from "react-router-dom";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Alert from 'react-bootstrap/Alert';
import Collapse from 'react-bootstrap/Collapse';
import { handleCall } from '../../services/AccessControlService'
import Loader from '../HelperComponent/Loader'
import Table from 'react-bootstrap/Table';
import { getCurrentLang} from '../../services/LinkService'
import UserContext from '../../services/UserContextProvider'
import {convertToReadableFormat, convertToApiFormat, convertToReadableFormatDateOnly} from '../../services/DateFormatter'
import DatePicker from 'react-date-picker';
import FieldType from '../../model/helper/FieldType'
import ToastCustom from '../HelperComponent/ToastCustom'
import ToastContainer from 'react-bootstrap/ToastContainer';
import { navbar } from '../../config/lang/fr';

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).listview


/**
 * Field components correspond to a displayable field in the entries table. It adapt the format to the data type (a date will be formated, a text will be displayed, ...)
 * It returns a <td> formatted
 */

const ScoreField = ({score}) => {
    return (
        <td className='align-middle'>{score}</td>
    )
}

const TextField = ({text}) => {
    return (
        <td className='align-middle'>{text}</td>
    )
}

const DateField = ({date}) => {
    return (
        <td className='align-middle'>{convertToReadableFormatDateOnly(date)}</td>
    )
}

const DateHourField = ({date}) => {
    return (
        <td className='align-middle'>{convertToReadableFormat(date)}</td>
    )
}

/**
 * Input components relate to the add entry form input fields. A DateInput will be a date picker, a score field will be a number input, ...
 */

const Input = ({children, label}) => {
    return (
        <Row className="mt-4">
            <Col className="col-4 mb-0 d-flex align-items-center flex-row-reverse">
                <Form.Label className="mb-0">{label + " :"}</Form.Label>
            </Col>
            <Col>
                {children}
            </Col>
        </Row>
    )
}

const TextInput = ({handleChange, name, value, label}) => {

    return (
        <Input label={label}>
            <Form.Control autoComplete="off" value={value} name={name}
                                onChange={handleChange} required type="text"  />
        </Input>
    )

}

const DateInput = ({handleChange, name, value, label}) => {

    const customHandleChange = (valueField) => {
        let event = {target: {name, value: valueField}}
        handleChange(event)
    }

    return (
        <Input label={label}>
            <DatePicker name={name} required onChange={customHandleChange} value={value} />
        </Input>
    )

}

const ScoreInput = ({handleChange, name, value, label}) => {

    return (
        <Input label={label}>
            <Form.Control autoComplete="off" value={value} name={name}
                                onChange={handleChange} required type="number"  />
        </Input>
    )

}

const TableEntry = ({children, id, handleDoubleClick}) => {

    const customHandleDoubleClick = (e) => {
        if(e.detail === 2) {
            handleDoubleClick(id)
        }
    }

    return (
        <tr key={id} onClick={customHandleDoubleClick} className="pointerTab">
            {children}
        </tr>
    )
}

const TableEntriesHeader = ({name}) => {

   return (
        <th>{name}</th>
   )

}

const ModalAddOrEditEntry = ({addEntryFunction, entryToAddOrEdit, show, handleClose, listId}) => {

    const [entry, setEntry] = useState(entryToAddOrEdit)
    const [userCtx, setUserCtx] = useContext(UserContext)
    const [responseContent, setResponseContent] = useState({})
    const isAnUpdate = useRef(false)

    useEffect(() => {
        setEntry({...entryToAddOrEdit})
        isAnUpdate.current = entryToAddOrEdit.entryName !== ""
        setResponseContent({})
    }, [entryToAddOrEdit])

    const handleChange = (event) => {

        const { name, value } = event.target

        if(name.startsWith("field")){
            let newEntry = {...entry}
            let item = newEntry[name]
            item["value"] = value
            setEntry(newEntry)
        } else {
            setEntry({...entry, [name]: value})
        }

    }

    const handleSubmit = async (event) => {
        event.preventDefault()

        // Build correct data for the body request required to add an entry
        let entryJson = {entryName : entry.entryName, fieldsWithValues: {}}

        for(let key in entry){
            if(key.startsWith("field")){
                let fieldId = key.substring(5)
                let value = entry[key].value

                // If it's a date, format it to the API desired format
                if(entry[key].type === FieldType.DATE){
                    value = convertToApiFormat(value)
                }

                entryJson["fieldsWithValues"][fieldId] = value
            }
        }

        // Sending to API if it's a creation
        let url = "", method = "";
        let isAnUpdate = entryToAddOrEdit.entryName !== ""
        if(!isAnUpdate){
            url = "/api/lists/" + listId + "/entries"
            method = "POST"
        } else {
            url = "/api/entries/" + entryToAddOrEdit.entryId
            method = "PUT"
        }

        let response = await handleCall(url, method, entryJson)

        if(response.data.needToLoginAgain){
            setUserCtx({...userCtx, needToLoginAgain: true})
        }

        if(response.success){
            addEntryFunction(response.data)
            handleClose()
        }

        if(response.errors){
            if(response.data.status === 500){
                setResponseContent({error: texts.errorServer})

            } else {
                setResponseContent(response.data)

            }
        }
    }


    let customFields = []
    for(let key in entry){

        if(!key.startsWith("field"))
            continue

        let item = entry[key]

        if(item.type === FieldType.TEXT){
            customFields.push(<TextInput key={key} handleChange={handleChange} name={key} label={item.name} value={item.value} />)
        }

        if(item.type === FieldType.DATE){
            customFields.push(<DateInput key={key} handleChange={handleChange} name={key} label={item.name} value={item.value} />)
        }

        if(item.type === FieldType.SCORE){
            customFields.push(<ScoreInput key={key} handleChange={handleChange} name={key} label={item.name} value={item.value} />)
        }
    }

    let isErrorsful = Object.keys(responseContent).length !== 0;

    return (
        <Modal size="xl" show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title className="text-info">{texts.addEntryTitleModal}</Modal.Title>
            </Modal.Header>
            <Modal.Body className="p-3">

                <Form onSubmit={handleSubmit}> 
                    <Form.Group>

                        <Row>
                            <Col className="col-4 mb-0 d-flex align-items-center flex-row-reverse">
                                <Form.Label className="mb-0">{texts.entryNameLabel}</Form.Label>
                            </Col>
                            <Col>
                                <Form.Control autoComplete="off" value={entry.entryName} name="entryName"
                                                onChange={handleChange} required type="text" placeholder={texts.entryNamePh}  />
                            </Col>
                        </Row>

                       <Row className=" mt-3 justify-content-center">
                            <Col className="col-10">
                                <hr />
                            </Col>
                       </Row>  
                        
                        {customFields}
                        
                    </Form.Group>

                    <Collapse className="mt-4 text-center" in={isErrorsful}>
                        <div>
                            <Alert variant="danger">
                                <ul className="mb-0">
                                    {
                                        Object.keys(responseContent).length !== 0 &&
                                        Object.keys(responseContent).map((keyName, keyIndex) => 
                                                <li key={keyIndex}>{responseContent[keyName]}</li>
                                        )
                                    }
                                </ul>
                            </Alert>
                        </div>
                    </Collapse>
            
                    <div className="mt-4 text-center">
                        <Button variant="secondary" type="submit">
                            {entryToAddOrEdit.entryName === "" ?
                                        texts.addEntryButtonModal
                                    :
                                        texts.modifyEntryButtonModal
                            }
                        </Button>
                    </div>
                
            </Form>
            </Modal.Body>
        </Modal>
    )
}

const ConfirmModal = ({show, textToDisplay, handleYes, handleClose}) => {

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
            <Modal.Title className="text-danger">{texts.titleSuppress}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p className="text-center"><i className="text-warning fa-3x fa-solid fa-triangle-exclamation"></i></p>
                <p className="text-center mb-0">{textToDisplay}</p>
            </Modal.Body>
            <Modal.Footer>
            <Button variant="outline-secondary" onClick={handleYes}>
                {texts.confirmSuppress}
            </Button>
            <Button variant="outline-info" onClick={handleClose}>
                {texts.cancelSuppress}
            </Button>
            </Modal.Footer>
        </Modal>
    )

}

const DeleteButton = ({handleClickFonction, id}) => {

    const handleClick = async (event) => {
        event.preventDefault() 
        handleClickFonction(id)
    }

    return (
        <>
            <Button onClick={handleClick} variant="danger"><i className="fa-solid fa-xmark"></i></Button>
        </>
    )

}

const ListViewPage = () => {

    const [userCtx, setUserCtx] = useContext(UserContext)
    const [toasts, setToasts] = useState([])
    const [list, setList] = useState({fields : [], entries: []})
    const [headers, setHeaders] = useState([])                          //      headers : [ {id: 1, header: <TableEntriesHeaders />}, ...  ]
    const [entryToAdd, setEntryToAdd] = useState({ entryName: "" })     //      EntryToAdd : {entryName: "", field1: {value: "", type= "score", name: "ma note"}, ...}
    const entryToAddLayout = useRef({ entryName: "" })                  // Layout used for reset
    const [entries, setEntries] = useState([])                          //      Table of Fields (TextField, DateField, ScoreField)
    const [sortByDate, setSortByDate] = useState(true)
    const [show, setShow] = useState(false);
    const [isLoading, setIsLoading] = useState(true)
    const params = useParams()
    const navigate = useNavigate();

    // Handle back to lists button
    const handleBackToListsButton = () => {
        navigate("/mesclassements")
    }

    // AddOrEditEntry modal params
    const handleClose = () => {
        setShow(false);

        // Reinit entry to add
        setEntryToAdd({...entryToAddLayout['current']})
    }
    const handleShow = () => setShow(true);
    const handleClickSort = (event) => {
        event.preventDefault()
        setSortByDate(!sortByDate)
    }
    const addEntryToList = (entry) => {
        // We first remove the entry if it already exists in list (in case of modification)
        removeEntryFromList(entry.id)

        list.entries.push(entry)
        setList({...list})

        // Reinit entry to add
        setEntryToAdd({...entryToAddLayout['current']})
    }

    // EditEntry params
    const handleDoubleClickOnEntry = (id) => {
        
        let entry = list.entries.find(item => item.id === id)

        let newEntryToAdd = {entryName: entry.entryName, entryId: id}

        entry.fieldValueList.forEach(item => {

            newEntryToAdd["field" + item.field.id] = {   
                                                type: item.field.type,
                                                name: item.field.fieldName,
                                                value : item.value
                                            }
        })

        setEntryToAdd(newEntryToAdd)
        handleShow()
    }


    // RemoveEntryConfirmation modal params
    const [showConfirm, setShowConfirm] = useState(false)
    const [idToDelete, setIdToDelete] = useState(-1)
    const [textToDisplayConfirm, setTextToDisplayConfirm] = useState("")

    const handleCloseConfirm = () => setShowConfirm(false);

    const handleShowConfirm = (id) => { 
        let entryName = list.entries.find(entry => entry.id === id).entryName
        setTextToDisplayConfirm(texts.textSuppress + entryName + " ?")
        setIdToDelete(id)
        setShowConfirm(true);
    }
    const removeEntryFromList = (entryId) => {
        list.entries = list.entries.filter(item => item.id !== entryId)
        setList({...list})
    }

    const handleYesConfirm = async () => {

        // Deleting entry
        let url = "/api/entries/" + idToDelete
        let response = await handleCall(url, "DELETE", {})

        if(response.success){
            removeEntryFromList(idToDelete)
            setToasts([...toasts, <ToastCustom title={texts.titleToast} content={texts.textSuppressSuccess} />])
        }

        if(response.errors || response.needToLoginAgain){
            setToasts([...toasts, <ToastCustom variant="danger" title={texts.titleToast} content={texts.textSuppressFail} />])
        }

        setIdToDelete(-1)
        handleCloseConfirm()
    }
    
    const updateEntries = () => {
        
        let newEntries = []
        let hasScoreFields = false

        //      Preparing entries
        list.entries.forEach(entry => {

            let entryFields = []

            //          Standards fields
            entryFields.push(<TextField key={-1} text={entry.entryName}/>)
            entryFields.push(<DateHourField key={-2} date={entry.creationDate}/>)

            //          Iterate over headers tab and adding the linked fieldvalue as a field in the tab
            let scoreTotal = 0
            headers.forEach(header => {

                let fieldId = header.id

                // If it's a generic field, we skip it.
                if(fieldId === -1){
                    return
                }

                // 
                let fieldValue = entry.fieldValueList.find(elt => elt.field.id === fieldId)
                let value = fieldValue.value
                let type = fieldValue.field.type
                if(type === FieldType.SCORE){

                    hasScoreFields = true
                    entryFields.push(<ScoreField key={fieldValue.id} score={value} />)
                    scoreTotal += value

                } else if (type === FieldType.TEXT){

                    entryFields.push(<TextField key={fieldValue.id} text={value} />)

                } else if (type === FieldType.DATE){

                    entryFields.push(<DateField key={fieldValue.id} date={value} />)

                }
            }) // end of headers.forEach

            //          Adding a score field
            if(hasScoreFields){
                entryFields.push(<ScoreField key={-3} score={scoreTotal} />)
            }

            // Adding buttons for actions
            let actionsField = (<td key={entry.id}>
                                    <DeleteButton handleClickFonction={handleShowConfirm} id={entry.id} />
                                </td>)
            entryFields.push(actionsField)
            

            let entryToAdd = (<TableEntry key={entry.id} id={entry.id} handleDoubleClick={handleDoubleClickOnEntry}>
                                {entryFields}
                            </TableEntry>
                            )
            newEntries.push({
                sumScore: scoreTotal,
                addedDate:  entry.creationDate,
                data : entryToAdd});  

        }) // end of list.entries.forEach

        setEntries(newEntries)

    }

    // Defining variable used in the render.
    // Preparing headers and skeleton for entryToAdd (in the addForm)
    // Retrieve user's list once component is mounted.
    useEffect(() => {
        (async function(){
            // Retrieve the list to edit
            let url = '/api/lists/' + params.listId
            const response = await handleCall(url, "GET", {});

            if(response.data.needToLoginAgain){
                setUserCtx({...userCtx, needToLoginAgain: true})
            }

            if(response.errors){
                setIsLoading(false)
            }
    

            // If success, then we adapt the headers, entryToAdd skeleton
            if(response.success){
                let hasScoreFields = false
                let futureList = response.data
                setList(futureList)
                setIsLoading(false)

                
                // Filling headers and EntryToAdd
                // Standards fields
                let newHeaders = []
                newHeaders.push({
                    id: -1,
                    header: <TableEntriesHeader key={-1} name={texts.tabEntryName}/>
                })
                newHeaders.push({
                    id: -1,
                    header: <TableEntriesHeader key={-2} name={texts.tabFieldDateAdded}/>
                })

                // Iterate over list's fields to create personnalized table.
                let scoreMax = 0
                futureList.fields.forEach(item => {

                    let name = item.fieldName
                    if(item.type === FieldType.SCORE){
                        hasScoreFields = true;
                        name = name + " (/" + item.scoreMax + ")"
                        scoreMax += item.scoreMax
                    }

                    newHeaders.push({
                        id: item.id,
                        header: <TableEntriesHeader key={item.id} name={name}/>
                    })

                    entryToAddLayout['current']["field" + item.id] = {   
                                                        type: item.type,
                                                        name: name,
                                                        value : ""
                                                    }
                })

                //          Adding a score field
                if(hasScoreFields){
                    let name = texts.tabFieldScoreTotal + " (/" + scoreMax + ")"
                    newHeaders.push({
                        id: -1,
                        header: <TableEntriesHeader key={-3} name={name}/>
                    })
                }   

                newHeaders.push({
                    id: -1,
                    header: <TableEntriesHeader key={-4} name={texts.tabActions}/>
                })
                
                setHeaders([...newHeaders])
                setEntryToAdd(structuredClone(entryToAddLayout['current']))
                updateEntries()
            }
        })();
    }, [])


    // Updating entries table when the list change (the list contains the entries)
    useEffect(updateEntries, [list])
    
    // If loading is completed and not list
    let isErrorsful = !isLoading && Object.keys(list).length <= 2;

    // Render form 
    return (

    <Container fluid className="main-container justify-content-center mb-5"> 

        {isErrorsful 
         ?
            <>
                <Row className="text-center justify-content-center">
                    <Col className="col-12 col-md-6">
                        <Alert variant="danger">
                            {texts.error}
                        </Alert>
                    </Col>
                </Row>
            </>
         :       
                <> 

                <Row className="text-center justify-content-center">
                    <Col bg="white" className="bg-white p-3 shadow col-md-9">

                            <h3 className="text-info mb-3">~ {list.listName} ~</h3>

                            <Row className="text-center justify-content-center">
                                <Col className="col-12 col-md-6">
                                    <Alert className="mt-3" variant="info">
                                        <i className="mb-4 fa-solid fa-circle-info fa-xl"></i>
                                        <p>{texts.descr}</p>
                                        <p className="mb-0">{texts.descr2}</p>
                                    </Alert>
                                </Col>
                            </Row>

                            <Row className="mb-4 mt-4">
                                <Col>
                                    <Button variant="success" onClick={handleBackToListsButton}><i className="fa-solid fa-arrow-left"></i> {texts.backward}</Button>
                                </Col>
                                <Col>
                                    <Button variant="secondary" onClick={handleShow}><i className="fa-solid fa-plus"></i> {texts.addEntry}</Button>
                                </Col>
                                <Col>
                                    <Button variant="info" onClick={handleClickSort}>
                                        <i className="fa-solid fa-rotate"></i> {sortByDate ? texts.switchToClassement : texts.switchToRecent}
                                    </Button>
                                </Col>
                            </Row>

                            <Table striped bordered hover>
                                <thead>
                                    <tr>
                                        {headers.map((value, index) => value.header)}
                                    </tr>
                                </thead>
                                <tbody>
                                        {sortByDate ?
                                            entries.sort((a,b) => a.addedDate < b.addedDate).map((value, index) => value.data)
                                            :
                                            entries.sort((a,b) => a.sumScore < b.sumScore).map((value, index) => value.data)
                                        }
                                </tbody>
                            </Table>

                            {(list.entries.length === 0 && !isLoading) && texts.empty }

                            <Loader isLoading={isLoading} />

                    </Col>
                </Row>
                <ModalAddOrEditEntry addEntryFunction={addEntryToList} entryToAddOrEdit={entryToAdd} show={show} handleClose={handleClose} listId={list.id} />
                <ConfirmModal textToDisplay={textToDisplayConfirm} show={showConfirm} handleYes={handleYesConfirm} handleClose={handleCloseConfirm} />
                <ToastContainer 
                    className="p-3" position="bottom-end">
                    {toasts}
                </ToastContainer>
        </>
        }

    </Container>
    )
}

export default ListViewPage;