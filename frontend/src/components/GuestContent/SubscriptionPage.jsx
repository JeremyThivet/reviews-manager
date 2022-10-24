import React from 'react';
import { getCurrentLang} from '../../services/LinkService'
import SubscriptionPageLayout from './SubscriptionPageLayout'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).subscriptionpage

const SubscriptionPage = () => {

    // Render form 
    return (
        <SubscriptionPageLayout texts={texts} />
    )
}

export default SubscriptionPage;