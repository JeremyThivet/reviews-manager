import React, {useState, useContext} from 'react';
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

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).listsManager


const NewListForm = () => {

    const [listName, setListName] = useState('')
    const [userCtx, setUserCtx] = useContext(UserContext);

    const handleChange = (event) => {
        const { name, value } = event.target
        setListName(value)
    }

    const submitForm = async (event) => {
        event.preventDefault()
        
        let url = "/api/users/" + userCtx.id + "/lists"
        let response = await handleCall(url, "POST", {listName})

        console.log(response);
        
    }

    let responseContent = {}
    let isErrorsful = false;
    let enableButton = listName.length !== 0;

    return (

        <Form noValidate onSubmit={submitForm}>
            {
            (!responseContent.success) &&
                <>
                
                <Form.Group controlId="formListName">
                    <Form.Label>{texts.listnameLabel}</Form.Label>
                    <Form.Control autoComplete="off"  name="listName" value={listName} onChange={handleChange} required type="text" placeholder={texts.listNamePh}
                            />
                </Form.Group>
            
                </>
            }
                    <Collapse in={isErrorsful}>
                        <div>
                        <Alert variant="danger">
                            <ul>
                                {
                                    responseContent.errors &&
                                    responseContent.errors.map((error, index) => 
                                            <li key={index}>{error}</li>
                                    )
                                }
                            </ul>
                        </Alert>
                        </div>
                    </Collapse>
        
            <div className="text-center">
            {
                <Button variant="secondary" type="submit" disabled={!enableButton}>
                    {texts.addButton}
                </Button>
            }
            </div>
            
        </Form>
    )
}


const ListsManagerPage = () => {

    // Render form 
    return (

    <Container fluid className="main-container justify-content-center"> 

        <Row className="text-center justify-content-center">
            <Col className="col-12 col-md-6">
                <h2>{texts.title}</h2>
            </Col>
        </Row>

        <Row className="text-center justify-content-center">
            <Col className="col-md-6">
                <NewListForm />
            </Col>
        </Row>

        <Row className="text-center justify-content-center">
            <Col className="col-md-9">
            <Table striped bordered hover>
                <thead>
                    <tr>
                    <th>#</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Username</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <td>1</td>
                    <td>Mark</td>
                    <td>Otto</td>
                    <td>@mdo</td>
                    </tr>
                    <tr>
                    <td>2</td>
                    <td>Jacob</td>
                    <td>Thornton</td>
                    <td>@fat</td>
                    </tr>
                    <tr>
                    <td>3</td>
                    <td colSpan={2}>Larry the Bird</td>
                    <td>@twitter</td>
                    </tr>
                </tbody>
                </Table>
            </Col>
        </Row>
    </Container>
    )
}

export default ListsManagerPage;