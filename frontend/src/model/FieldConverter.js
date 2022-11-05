
 import { getCurrentLang } from '../services/LinkService'
 
 let langAcron = getCurrentLang()
 let texts = require('../config/lang')(langAcron).field

  export function convertFieldToDisplayable(field) {
    let result = {...field}
    result.fieldType = texts[result.fieldType.toLowerCase()]

    result.autres = {}

    if(field.fieldType === "SCORE"){
        result.autres.scoreMax = field.scoreMax
        delete result.scoreMax
    }

    return result
}


