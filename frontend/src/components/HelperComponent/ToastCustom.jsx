import Toast from 'react-bootstrap/Toast';
import ToastContainer from 'react-bootstrap/ToastContainer';
import React, { useState } from 'react';
import { getCurrentLang} from '../../services/LinkService'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).toast

const ToastCustom = ({title, content, variant}) => {

    const [show, setShow] = useState(true)

    const handleClose = () => {
        setShow(false);
    }

    return (
            <Toast bg={variant ? variant : "Light"} onClose={handleClose} show={show} delay={5000} autohide>
            <Toast.Header>
                <strong className="me-auto">{title}</strong>
                <small>{texts.now}</small>
            </Toast.Header>
            <Toast.Body className={variant === 'danger' && 'text-white'}>{content}</Toast.Body>
            </Toast>
    )
}

export default ToastCustom;
