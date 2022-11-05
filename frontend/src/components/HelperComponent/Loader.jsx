import Spinner from 'react-bootstrap/Spinner';
import { getCurrentLang} from '../../services/LinkService'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).loader

const Loader = ({isLoading}) => {

    if(isLoading) {
        return  (
        <>
            <Spinner animation="border" />
            <p>{texts.text}</p>
        </>
        

        )
    }

}

export default Loader;