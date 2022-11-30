import React, { useState } from 'react';
import { getCurrentLang} from '../../services/LinkService'
import { getAppVersion } from '../../services/GeneralService'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).footer


class Footer extends React.Component {

    constructor(props){
        super(props)
        this.state = {
            version: "N/A"
        };
    }

    componentDidMount(){
        this.fetchVersion();
    }

    async fetchVersion(){
        let retrievedVersion = await getAppVersion()
        this.setState({version: retrievedVersion})
    }

    render() {

        return (
                <footer className="bg-dark  text-center text-white mt-auto footer mt-auto py-3">
                    
                    <section className="pt-1 topfoot mb-0 pb-1">
                        <p className="mb-0">
                            {texts.allrights} - Version {this.state.version}
                        </p>
                    </section>

                 </footer>
        )
    }

}


export default Footer;