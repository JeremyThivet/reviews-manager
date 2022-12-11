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
import ToastContainer from 'react-bootstrap/ToastContainer';
import validateField from '../../policies/FieldPolicies'
import ErrorMessageInForm from '../HelperComponent/ErrorMessageInForm'
import { CustomizableTable, CustomizableTableEntry } from '../HelperComponent/CustomizableTable'
import { DeleteButton } from '../HelperComponent/CustomButtons'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).editList

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
                                        <ErrorMessageInForm errors={formErrors.fieldName} />
                                    </div>
                                </Collapse>
                            </Form.Group>

                            <label>{texts.labelType}</label>
                            <Form.Select value={fieldToAdd.type} className="mb-3 mt-2" name="type" onChange={handleChange}>
                                <option value="">{texts.optPh}</option>
                                <option value="text">{texts.opt1}</option>
                                <option value="date">{texts.opt2}</option>
                                <option value="score">{texts.opt3}</option>
                            </Form.Select>
                            <Collapse in={errorsInType} >
                                    <div>
                                        <ErrorMessageInForm errors={formErrors.type} />
                                    </div>
                            </Collapse>

                            {fieldToAdd.type === "score" &&
                                <Form.Group >
                                    <Form.Label>{texts.scoreMaxLabel}</Form.Label>
                                    <Form.Control autoComplete="off"  name="scoreMax" value={fieldToAdd.scoreMax} onChange={handleChange} required type="number" />
                                    <Collapse in={errorsInScoreMax} className="mt-3">
                                        <div>
                                            <ErrorMessageInForm errors={formErrors.scoreMax} />
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

    // Preparing table and building
    let headers = [texts.tabFieldName, texts.tabFieldType, texts.tabFieldAutres, texts.tabFieldActions]
    let buttonToastsTexts = {successDeletedTitle: texts.successDeletedTitle,
                             successDeletedContent: texts.successDeletedContent,
                             failDeletedTitle: texts.failDeletedTitle,
                             failDeletedContent: texts.failDeletedContent
                            }
    let items = [];
    items = list.fields.map((item, key) => {
                                let convertedItem = convertFieldToDisplayable(item)
                                let autres = [];
                                for(const key in convertedItem.autres){
                                    autres.push(<li key={key}>{texts[key]} : {convertedItem.autres[key]}</li>)
                                }

                                let fields = [convertedItem.fieldName, convertedItem.fieldType, autres.length === 0 ? " - " : autres]

                                let buttons = [<DeleteButton key="2" url="/api/fields/" texts={buttonToastsTexts} actionFunction={removeFromArray} 
                                              toastModifier={[toasts, setToasts]} id={item.id} />]

                                return (<CustomizableTableEntry key={key} fields={fields} buttons={buttons} />)
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

                                        <CustomizableTable headers={headers} entries={items} />

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