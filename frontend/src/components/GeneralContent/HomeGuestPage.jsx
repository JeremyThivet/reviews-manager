import React from 'react';
import { Row, Col, Container, Alert } from 'react-bootstrap';
import { getCurrentLang} from '../../services/LinkService'
let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).homeguest

const HomeGuestPage = () => {

    return (
        <Container>
            <Row className="text-center justify-content-center">
                <Col className="col-12 col-md-6">
                    <h2 className="text-center mt-2 mb-4">{texts.title}</h2>
                    <Alert variant="info">{texts.description}</Alert>
                </Col>
            </Row>
        </Container>
    )

}

export default HomeGuestPage;