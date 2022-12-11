import React, {useState, useContext} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Alert from 'react-bootstrap/Alert';
import Collapse from 'react-bootstrap/Collapse';
import Card from 'react-bootstrap/Card';
import { loginUser } from '../../services/AccessControlService'
import { getCurrentLang} from '../../services/LinkService'
import UserContext from '../../services/UserContextProvider'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).connection

const ConnectionForm = () => {

    const initialFormState = {username: '', password:'', stayConnected: false}
    const [user, setUser] = useState(initialFormState);
    const [userCtx, setUserCtx] = useContext(UserContext);

    const [responseContent, setResponseContent] = useState({errors : [], success: false})

    const handleChange = (event) => {
        const { name, value } = event.target

        let valueToAssign = name === "stayConnected" ? event.target.checked : value

        let newUser = {...user, [name]: valueToAssign}
        
        setUser(user => (newUser))
    }

    const submitForm = async (event) => {
        event.preventDefault()
        
        let response = await loginUser(user)
        setResponseContent(response)
        
        if(response.success){
            setUserCtx(response.data)
        }
    }
    
    let isSuccessful = responseContent.success ? true : false
    let isErrorsful  = responseContent.errors.length !== 0 ? true : false
    let enableButton = user.password !== '' && user.username !== ''

    // Render form 
    return (

    <Container fluid className="main-container justify-content-center"> 

        <Row className="text-center justify-content-center">
            <Col className="col-12 col-md-6">
                <h2 className="text-center mt-2 mb-4">{texts.title}</h2>
                <Alert variant="info">{texts.description}</Alert>
            </Col>
        </Row>

        <Row className="mt-4 text-left justify-content-center">
            <Col className="col-12 col-md-3">
                <Card className="transparent-card p-3 pt-4 shadow-sm">
                    <Card.Body>
                        <Form noValidate onSubmit={submitForm}>
                            {
                            (!responseContent.success) &&
                                <>
                                
                                <Form.Group controlId="formUsername">
                                    <Form.Label>{texts.usernameLabel}</Form.Label>
                                    <Form.Control autoComplete="off"  name="username" value={user.username} onChange={handleChange} required type="text" placeholder={texts.usernamePh}
                                         />
                                </Form.Group>

                                <Form.Group className="mt-3" controlId="formPass">
                                    <Form.Label >{texts.passLabel}</Form.Label>
                                    <Form.Control name="password" value={user.password} onChange={handleChange} required type="password" placeholder={texts.passPh} 
                                     />
                                </Form.Group>
                                
                                <Form.Group className="mt-3" controlId="formStayConnected">
                                    <Form.Check type="checkbox" name="stayConnected" onChange={handleChange} label={texts.stayConnected} />
                                </Form.Group>
 
                                <hr className ="mt-4 mb-4"></hr>
                            
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
                                    <Collapse in={isSuccessful}>
                                        <div>
                                        <Alert variant="success">
                                            <h5 className="text-center mb-3">{texts.successTitle}</h5>
                                            <p className="text-center">{texts.success}</p>
                                        </Alert>
                                        </div>
                                    </Collapse>
                        
                            <div className="text-center">
                            {
                                (!responseContent.success) &&
                                <Button variant="secondary" type="submit" disabled={!enableButton}>
                                    {texts.buttonTitle}
                                </Button>
                            }
                            </div>
                            
                        </Form>
                    </Card.Body>
                </Card>
            </Col>
        </Row>
    </Container>
    )
}



export default ConnectionForm;