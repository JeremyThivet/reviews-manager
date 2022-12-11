import { Button } from "react-bootstrap"
import ToastCustom from "./ToastCustom"
import { handleCall } from '../../services/AccessControlService'
import {Link} from "react-router-dom";

/**
 * 
 * @param {object} texts represents the texts used in the toasts. {successDeletedTitle, successDeletedContent, failDeletedTitle, failDeletedContent} 
 * @returns 
 */
export const DeleteButton = ({url, actionFunction, id, toastModifier, texts}) => {

    const handleClick = async (event) => {
        event.preventDefault()  

        let [toasts, setToasts] = toastModifier
        let urlToHit = url + id
        let response = await handleCall(urlToHit, "DELETE", {})

        if(response.success){
            actionFunction(id)
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


export const EditButton = ({id, url}) => {

    let urlToNavigateTo = url + id

    return (<Link to={urlToNavigateTo}>
                <Button variant="info"><i className="fa-solid fa-pencil fa-sm"></i></Button>
            </Link>)

}