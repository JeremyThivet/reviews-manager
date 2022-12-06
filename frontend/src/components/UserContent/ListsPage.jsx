import React, {useState, useContext, useEffect} from 'react';
import {Link, useNavigate} from "react-router-dom";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Alert from 'react-bootstrap/Alert';
import Collapse from 'react-bootstrap/Collapse';
import { handleCall } from '../../services/AccessControlService'
import Loader from '../HelperComponent/Loader'
import Table from 'react-bootstrap/Table';
import { getCurrentLang} from '../../services/LinkService'
import UserContext from '../../services/UserContextProvider'
import {convertToReadableFormat} from '../../services/DateFormatter'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).lists


const TabListItem = ({item}) => {

    const navigate = useNavigate();

    const handleClick = (event) => {
        event.preventDefault()
        let url = "/consulterclassement/" + item.id
        navigate(url)
    }

    return (
            <tr className="pointerTab" onClick={handleClick}>
                    <td>{item.listName}</td>
                    <td>{convertToReadableFormat(item.lastUpdate)}</td>
                    <td>{item.numberOfEntries}</td>
                </tr>

    )
}


const ListsPage = () => {

    const [userCtx, setUserCtx] = useContext(UserContext)
    const [userLists, setUserLists] = useState([])
    const [isLoading, setIsLoading] = useState(true)

    // Retrieve user's list once component is mounted.

    useEffect(() => {
        (async function(){
            let url = "/api/users/" + userCtx.id + "/lists-sumup";
            const res = await handleCall(url, "GET", {});

            // Returns an array of lists objects if successful.
            if(res.success){
                setIsLoading(false)
                setUserLists(res.data)
            }
        })()
      }, [])


    // Preparing table
    let items = [];
    items = userLists.map((item, key) => 
                                <TabListItem key={key}
                                    item={item}/>
                            
                         )

    // Render form 
    return (

    <Container fluid className="main-container justify-content-center mb-5"> 

        <Row className="text-center justify-content-center">
            <Col className="col-12 col-md-6">
                <h2>{texts.title}</h2>
                <Alert className="mt-3" variant="info">
                    <i className="mb-4 fa-solid fa-circle-info fa-xl"></i>
                    <p>{texts.descr}</p>
                    <p className="mb-0">{texts.descr2}</p>
                </Alert>
            </Col>
        </Row>

        <Row className="mt-4 text-center justify-content-center">
            <Col className="col-md-9">
                <Table responsive striped bordered hover>
                    <thead>
                        <tr>
                            <th>{texts.tabListName}</th>
                            <th>{texts.tabListUpdateDate}</th>
                            <th>{texts.tabListNumberEntries}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {   userLists.length !== 0 && items
                        }
                    </tbody>
                </Table>
                <Loader isLoading={isLoading} />

            </Col>
        </Row>

    </Container>
    )
}

export default ListsPage;