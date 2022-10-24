import React from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Alert from 'react-bootstrap/Alert';
import SubscriptionForm from './SubscriptionForm'

const SubscriptionPageLayout = ({texts}) => {

    // Render form 
    return (
        <Container fluid className="justify-content-center"> 

            <Row className="text-center justify-content-center">
                <Col className="col-12 col-md-6">
                    <h2 className="text-center mt-2 mb-4">{texts.title}</h2>
                    <Alert variant="info">{texts.description}</Alert>
                </Col>
            </Row>
            
            <SubscriptionForm />

        </Container>

    )
}

export default SubscriptionPageLayout;