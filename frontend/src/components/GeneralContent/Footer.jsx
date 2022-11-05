import React, { useState } from 'react';
import { getCurrentLang} from '../../services/LinkService'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).footer


class Footer extends React.Component {

    render() {

        let version = "1.0";

        return (
                <footer className="bg-dark  text-center text-white mt-auto footer mt-auto py-3">
                    
                <section className="pt-1 topfoot mb-0 pb-1">
                <p className="mb-0">
                   {texts.allrights} - Version {version}
                </p>
                </section>

                    </footer>
        )
    }

}


export default Footer;