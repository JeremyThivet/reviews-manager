import React, {useState, useContext, useEffect} from 'react';
import { useParams } from "react-router-dom";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Card from 'react-bootstrap/Card';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Alert from 'react-bootstrap/Alert';
import Collapse from 'react-bootstrap/Collapse';
import Table from 'react-bootstrap/Table';
import { getCurrentLang} from '../../services/LinkService'
import UserContext from '../../services/UserContextProvider'
import { handleCall } from '../../services/AccessControlService'
import Loader from '../HelperComponent/Loader'
import { convertFieldToDisplayable } from '../../model/FieldConverter'
import ToastCustom from '../HelperComponent/ToastCustom'
import ToastContainer from 'react-bootstrap/ToastContainer';
import validateField from '../../policies/FieldPolicies'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).editList

const ErrorMessage = ({errors}) => {

    let isThereErrors = errors.length > 0
    if(isThereErrors){
        return (
                    <ul className='text-danger'>
                    {
                        errors.map((error, key) => ( <li key={key}>{error}</li> ))
                    }
                    </ul>
        )
    }
}

const AddNewFieldForm = ({addFieldInTab}) => {

    const intialStateFieldToAdd = {type: '', fieldName: '', scoreMax: '1', displayOption: '0'}
    const [fieldToAdd, setFieldToAdd] = useState(intialStateFieldToAdd)
    const [formErrors, setFormErrors] = useState({type: '', fieldName: '', scoreMax: '', displayOption: '0'})
    const params = useParams()
    const [responseContent, setResponseContent] = useState({})
    const [userCtx, setUserCtx] = useContext(UserContext);
    const [isErrorsful, setIsErrorsful] = useState(false)
    

    const handleChange = (event) => {
        const { name, value } = event.target
        let newFieldToAdd = {...fieldToAdd, [name]: value}
        setFieldToAdd(newFieldToAdd)
    }

    const submitForm = async (event) => {
        event.preventDefault()

        // Formatting data for API
        let dataToSend = {...fieldToAdd};
        let isScoreField = dataToSend.type === "score"
        
        // validating datas
        let errors = validateField(dataToSend, langAcron)
        if(errors){
            setFormErrors({...intialStateFieldToAdd, ...errors})
            return
        }


        let url = "/api/lists/" + params.listId + "/fields?type=" + fieldToAdd.type 
        
        if(!isScoreField) {
            delete dataToSend.scoreMax
            delete dataToSend.displayOption
        }
        delete dataToSend.type

        let response = await handleCall(url, "POST", dataToSend)

        if(response.data.needToLoginAgain){
            setUserCtx({...userCtx, needToLoginAgain: true})
        }

        if(response.success){
            // Build a new field and add it to table using ListEditPage function.
            let field = {   id: response.data.id, 
                            fieldName: response.data.fieldName,
                            fieldType: response.data.fieldType,
                        }
            if(isScoreField){
                field.scoreMax = dataToSend.scoreMax
            }

            addFieldInTab(field)
            
            setFieldToAdd(intialStateFieldToAdd)
        }

        if(response.errors){
            setIsErrorsful(true)
            setResponseContent(response.data)
        }
        
    }

    let enableButton = true
    let errorsInFieldName = formErrors.fieldName.length > 0
    let errorsInScoreMax = formErrors.scoreMax.length > 0
    let errorsInType = formErrors.type.length > 0

    return (

        <Row className="justify-content-center">
            <Col className="col-6 border p-4">

                <h3 className="text-info mb-4 fs-5">{texts.addTitle}</h3>

                <Form noValidate onSubmit={submitForm}>
                        <>
                    
                        <Form.Group controlId="formAddField">

                            <Form.Group className="mb-3">
                                <Form.Label>{texts.nameLabel}</Form.Label>
                                <Form.Control autoComplete="off"  name="fieldName" value={fieldToAdd.fieldName} onChange={handleChange} required type="text" placeholder={texts.namePh} />
                                <Collapse in={errorsInFieldName} className="mt-3">
                                    <div>
                                        <ErrorMessage errors={formErrors.fieldName} />
                                    </div>
                                </Collapse>
                            </Form.Group>

                            <label>{texts.labelType}</label>
                            <Form.Select className="mb-3 mt-2" name="type" onChange={handleChange}>
                                <option value="">{texts.optPh}</option>
                                <option value="text">{texts.opt1}</option>
                                <option value="date">{texts.opt2}</option>
                                <option value="score">{texts.opt3}</option>
                            </Form.Select>
                            <Collapse in={errorsInType} >
                                    <div>
                                        <ErrorMessage errors={formErrors.type} />
                                    </div>
                            </Collapse>

                            {fieldToAdd.type === "score" &&
                                <Form.Group >
                                    <Form.Label>{texts.scoreMaxLabel}</Form.Label>
                                    <Form.Control autoComplete="off"  name="scoreMax" value={fieldToAdd.scoreMax} onChange={handleChange} required type="number" />
                                    <Collapse in={errorsInScoreMax} className="mt-3">
                                        <div>
                                            <ErrorMessage errors={formErrors.scoreMax} />
                                        </div>
                                    </Collapse>
                                </Form.Group>
                            }

                        </Form.Group>
                    
                        </>
                    
                            <Collapse className="mt-2" in={isErrorsful}>
                                <div>
                                <Alert variant="danger">
                                    <ul className="mb-0">
                                        {
                                            texts.errorAdd
                                        }
                                    </ul>
                                </Alert>
                                </div>
                            </Collapse>
                
                    <div className="text-center">
                    {
                        <Button className="mt-4" variant="secondary" type="submit" disabled={!enableButton}>
                            {texts.addButton}
                        </Button>
                    }
                    </div>
                    
                </Form>
            </Col>
        </Row>

    )

}

const DeleteButton = ({deleteItem, id, toastModifier}) => {

    const handleClick = async (event) => {
        event.preventDefault()  

        let url = "/api/fields/" + id
        let response = await handleCall(url, "DELETE", {})

        if(response.success){
            deleteItem(id)
            toastModifier[1]([...toastModifier[0], <ToastCustom title={texts.successDeletedTitle} content={texts.successDeletedContent} />])
        }

        if(response.errors || response.needToLoginAgain){
            toastModifier[1]([...toastModifier[0], <ToastCustom variant="danger" title={texts.failDeletedTitle} content={texts.failDeletedContent} />])
        }
        
    }

    return (
        <Button onClick={handleClick} variant="danger"><i className="fa-solid fa-xmark"></i></Button>
    )

}

const TabFieldItem = ({item, deleteButton}) => {

    let autres = [];
    for(const key in item.autres){
        autres.push(<li key={key}>{texts[key]} : {item.autres[key]}</li>)
    }

    return (
                <tr>
                    <td>{item.fieldName}</td>
                    <td>{item.fieldType}</td>
                    <td>{autres.length ===  0 ? " - " : autres}</td>
                    <td>{deleteButton}</td>
                </tr>

    )
}


const ListEditPage = () => {

    const [userCtx, setUserCtx] = useContext(UserContext);
    const [toasts, setToasts] = useState([])
    const params = useParams()
    const [list, setList] = useState({fields : []})
    const [isLoading, setIsLoading] = useState(true)

    const removeFromArray = (id) => {
        list.fields = list.fields.filter(item => item.id !== id)
        setList(list)
    }

    const addFieldInTab = (field) => {
        list.fields.push(field)
        setList({...list})
    }

    useEffect(() => {
            (async function(){
                // Retrieve the list to edit
                let url = '/api/lists/' + params.listId
                const response = await handleCall(url, "GET", {});
                console.log(response)

                if(response.data.needToLoginAgain){
                    setUserCtx({...userCtx, needToLoginAgain: true})
                }
        
                if(response.success){
                    setList(response.data)
                    setIsLoading(false)
                }
        
                if(response.errors){
                    setIsLoading(false)
                }
            })();
    }, [])

    console.log(list)

    // Preparing table
    let items = [];
    items = list.fields.map((item, key) => {
                                let convertedItem = convertFieldToDisplayable(item)
                                return <TabFieldItem key={key}
                                    deleteButton={<DeleteButton deleteItem={removeFromArray} toastModifier={[toasts, setToasts]} id={item.id} />}
                                    item={convertedItem}/>
                                }  
                            
                         )

    // If loading is completed and not list
    let isErrorsful = !isLoading && Object.keys(list).length <= 1;

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
                    <Col className="col-12 col-md-6 mb-2">
                        <h2>{texts.title}</h2>
                    </Col>
                </Row>

                <Row className="text-center justify-content-center">
                    <Col className="col-md-9">

                        <Card className="p-3 pt-4 shadow-sm">
                                <Card.Body>
                                        <h3 className="text-info mb-3">{list.listName}</h3>

                                        <Table striped bordered hover>
                                            <thead>
                                                <tr>
                                                    <th>{texts.tabFieldName}</th>
                                                    <th>{texts.tabFieldType}</th>
                                                    <th>{texts.tabFieldAutres}</th>
                                                    <th>{texts.tabFieldActions}</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {   list.fields.length !== 0 && items
                                                }
                                            </tbody>
                                        </Table>

                                        {(list.fields.length === 0 && !isLoading) && texts.empty }

                                        <Loader isLoading={isLoading} />

                                        <hr className ="mt-4 mb-4"></hr>

                                        <AddNewFieldForm addFieldInTab={addFieldInTab} />
                                        
                                </Card.Body>
                        </Card>

                        <ToastContainer 
                            className="p-3" position="bottom-end">
                            {toasts}
                        </ToastContainer>

                    </Col>
                </Row>
        </>
        }

    </Container>
    )
}

export default ListEditPage;