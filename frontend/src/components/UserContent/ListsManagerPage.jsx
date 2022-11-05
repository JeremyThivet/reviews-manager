import React, { useState, useContext, useEffect } from 'react';
import {Link, useNavigate} from "react-router-dom";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
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
import ToastCustom from '../HelperComponent/ToastCustom'
import ToastContainer from 'react-bootstrap/ToastContainer';
import { toast } from '../../config/lang/fr';

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).listsManager


const NewListForm = () => {

    const [listName, setListName] = useState('')
    const [responseContent, setResponseContent] = useState({})
    const [userCtx, setUserCtx] = useContext(UserContext);
    const navigate = useNavigate();

    const handleChange = (event) => {
        const { name, value } = event.target
        setListName(value)
    }

    const submitForm = async (event) => {
        event.preventDefault()  

        let url = "/api/users/" + userCtx.id + "/lists"
        let response = await handleCall(url, "POST", {listName})

        if(response.data.needToLoginAgain){
            setUserCtx({...userCtx, needToLoginAgain: true})
        }

        if(response.success){
            let url = "/editerclassement/" + response.data.id
            navigate(url)
        }

        if(response.errors){
            if(response.data.status === 500){
                setResponseContent({error: texts.errorServer})

            } else {
                setResponseContent(response.data)

            }
        }
        
    }

    let isErrorsful = Object.keys(responseContent).length !== 0;
    let enableButton = listName.length !== 0;

    return (

        <Form noValidate onSubmit={submitForm}>
                <>
                
                <Form.Group controlId="formListName">
                    <Form.Label>{texts.listnameLabel}</Form.Label>
                    <Form.Control autoComplete="off"  name="listName" value={listName} onChange={handleChange} required type="text" placeholder={texts.listNamePh}
                            />
                </Form.Group>
            
                </>
            
                    <Collapse className="mt-2" in={isErrorsful}>
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
        
            <div className="mt-3 text-center">
            {
                <Button variant="secondary" type="submit" disabled={!enableButton}>
                    {texts.addButton}
                </Button>
            }
            </div>
            
        </Form>
    )
}

const EditButton = ({id}) => {

    let url = "/editerclassement/" + id

    return (<Link to={url}>
                <Button variant="info"><i className="fa-solid fa-pencil fa-sm"></i></Button>
            </Link>)

}

const DeleteButton = ({deleteItem, id, toastModifier}) => {

    const handleClick = async (event) => {
        event.preventDefault()  

        let url = "/api/lists/" + id
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
        <Button onClick={handleClick} variant="danger"><i className="fa-solid fa-xmark fa-lg"></i></Button>
    )

}

const TabListItem = ({item, deleteButton, editButton}) => {

    return (
                <tr>
                    <td>{item.listName}</td>
                    <td>{item.creationDate}</td>
                    <td>{item.lastUpdate}</td>
                    <td>{editButton} {deleteButton}</td>
                </tr>

    )
}


const ListsManagerPage = () => {

    const [userCtx, setUserCtx] = useContext(UserContext)
    const [toasts, setToasts] = useState([])
    const [userLists, setUserLists] = useState([])
    const [isLoading, setIsLoading] = useState(true)

    const removeFromArray = (id) => setUserLists(userLists.filter(item => item.id !== id))
    


    // Retrieve user's list once component is mounted.

    useEffect(() => {
        (async function(){
            let url = "/api/users/" + userCtx.id + "/lists";
            const res = await handleCall(url, "GET", {});

            // Returns an array of lists objects if successful.
            if(res.success){
                setIsLoading(false)
                setUserLists(res.data)
            }
        })()
      }, [])


    // Preparing table
    let items = [];
    items = userLists.map((item, key) => 
                                <TabListItem key={key}
                                    deleteButton={<DeleteButton deleteItem={removeFromArray} toastModifier={[toasts, setToasts]} id={item.id} />}
                                    editButton={<EditButton id={item.id} />}
                                    item={item}/>
                            
                         )
    // Render form 
    return (

    <Container fluid className="main-container justify-content-center mb-5"> 

        <Row className="text-center justify-content-center">
            <Col className="col-12 col-md-6">
                <h2>{texts.title}</h2>
            </Col>
        </Row>

        <Row className="text-center justify-content-center">
            <Col className="col-md-6 mt-3">
                <NewListForm />
            </Col>
        </Row>


        <Row className="text-center justify-content-center">
            <Col className="col-md-9">
                <hr className ="mt-4 mb-5"></hr>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>{texts.tabListName}</th>
                            <th>{texts.tabListCreationDate}</th>
                            <th>{texts.tabListUpdateDate}</th>
                            <th>{texts.tabListActions}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {   userLists.length !== 0 && items
                        }
                    </tbody>
                </Table>
                <Loader isLoading={isLoading} />

                
                <ToastContainer 
                
                className="p-3" position="bottom-end">
                    {toasts}
                </ToastContainer>
            </Col>
        </Row>
    </Container>
    )
}

export default ListsManagerPage;