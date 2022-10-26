import React, {useState, useContext} from 'react';
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

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).editList

const ListEditPage = () => {

    // Render form 
    return (

    <Container fluid className="main-container justify-content-center"> 

        <Row className="text-center justify-content-center">
            <Col className="col-12 col-md-6">
                <h2>{texts.title}</h2>
            </Col>
        </Row>

        <Row className="text-center justify-content-center">
            <Col className="col-md-9">

            <Card className="transparent-card p-3 pt-4 shadow-sm">
                    <Card.Body>

                        
                    </Card.Body>
            </Card>


            <Button href="creerclassement" variant="secondary">{texts.addButton}</Button>
            </Col>
        </Row>

    </Container>
    )
}

export default ListEditPage;