import React from 'react';
import { Row, Col, Container } from 'react-bootstrap';
import { getCurrentLang} from '../../services/LinkService'
let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).legalnotice

const LegalNoticePage = () => {

    return (
        <Container>
            <Row className="justify-content-center">
                <Col className="col-12 col-md-10">
                    <h2 className="text-center mt-2 mb-4">{texts.title}</h2>
                    <p className="mt-5">{texts.description}</p>

                    <h2 className="mt-4 h6 fw-bold">{texts.titleHeb}</h2>
                    <p>{texts.descrHeb} <br /> <a target="_blank" href={texts.websiteHeb}>{texts.textLinkHeb}</a></p>
                    <p>{texts.descr2Heb}</p>

                    <h2 className="mt-4 h6 fw-bold">{texts.titleCookies}</h2>
                    <p>{texts.descrCookies}</p>
                </Col>
            </Row>
        </Container>
    )

}

export default LegalNoticePage;