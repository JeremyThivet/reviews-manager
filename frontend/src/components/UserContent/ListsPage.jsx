import React, {useState, useContext} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Alert from 'react-bootstrap/Alert';
import Collapse from 'react-bootstrap/Collapse';
import Card from 'react-bootstrap/Card';
import { getCurrentLang} from '../../services/LinkService'
import UserContext from '../../services/UserContextProvider'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).lists

const ListsPage = () => {

    // Render form 
    return (

    <Container fluid className="main-container justify-content-center"> 

        <Row className="text-center justify-content-center">
            <Col className="col-12 col-md-6">
                <h2>Vos listes</h2>
            </Col>
        </Row>
    </Container>
    )
}

export default ListsPage;