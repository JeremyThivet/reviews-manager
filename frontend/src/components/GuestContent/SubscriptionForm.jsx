import React, {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Alert from 'react-bootstrap/Alert';
import Collapse from 'react-bootstrap/Collapse';
import { submitUser } from '../../services/UserService'
import { getCurrentLang} from '../../services/LinkService'
import validateUser from '../../policies/UserPolicies'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).register

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

const SubscriptionForm = () => {

    const initialFormState = {username: '', password:'', repass:''}
    const [user, setUser] = useState(initialFormState);

    const [formErrors, setFormErrors] = useState(initialFormState)

    const [responseContent, setResponseContent] = useState({})

    const handleChange = (event) => {
        // Updating user
        const { name, value } = event.target
        let newUser = {...user, [name]: value}
        setUser(user => (newUser))

        // Validating data
        let errors = validateUser(newUser, langAcron)
        setFormErrors({...initialFormState, ...errors})
    }

    const submitForm = async (event) => {
        event.preventDefault()
        let response = await submitUser(user)
        setResponseContent(response)
    }
    

    let errorsInUsername = formErrors.username.length > 0
    let errorsInPass = formErrors.password.length > 0
    let errorsInRepass = formErrors.repass.length > 0
    let enableButton = (!errorsInPass && !errorsInRepass && !errorsInUsername && user.username !== '')
    let isSuccessful = responseContent.success ? true : false
    let isErrorsful  = responseContent.errors ? true : false

    // Render form 
    return (

        <Row className="text-left justify-content-center">
            <Col className="col-12 col-md-3">
                <Form noValidate onSubmit={submitForm}>
                    {
                    (!responseContent.success) &&
                        <>
                        
                        <Form.Group controlId="formUsername">
                            <Form.Label>{texts.usernameLabel}</Form.Label>
                            <Form.Control autoComplete="off"  name="username" value={user.username} onChange={handleChange} required type="text" placeholder={texts.usernamePh}
                                isValid={!errorsInUsername && user.username !== ''} isInvalid={errorsInUsername} />
                                <Collapse in={errorsInUsername} >
                                    <div>
                                        <ErrorMessage errors={formErrors.username} />
                                    </div>
                                </Collapse>
                        </Form.Group>

                        <Form.Group className="mt-3" controlId="formPass">
                            <Form.Label >{texts.passLabel}</Form.Label>
                            <Form.Control name="password" value={user.password} onChange={handleChange} required type="password" placeholder={texts.passPh} 
                            isValid={!errorsInPass  && user.password !== ''} isInvalid={errorsInPass} />
                            <Collapse in={errorsInPass} >
                                <div>
                                    <ErrorMessage errors={formErrors.password} />
                                </div>
                            </Collapse>
                        </Form.Group>

                        <Form.Group className="mt-3" controlId="formRepass"> 
                            <Form.Label >{texts.repassLabel}</Form.Label>
                            <Form.Control name="repass" value={user.repass} onChange={handleChange} required type="password" placeholder={texts.passPh} 
                            isValid={!errorsInRepass  && user.repass !== ''} isInvalid={errorsInRepass} />
                            <Collapse in={errorsInRepass} >
                                    <div>
                                        <ErrorMessage errors={formErrors.repass} />
                                    </div>
                                </Collapse>
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
                                    <h5 className="text-center mb-3">{texts.congrats}</h5>
                                    <p>{texts.congratsDesc}</p>
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
            </Col>
        </Row>
    )
}



export default SubscriptionForm;