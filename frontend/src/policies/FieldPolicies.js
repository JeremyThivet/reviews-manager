/**
 * See https://validatejs.org/ - Very easy to use
 */
 function validateDatas(datas, lang){

    let validate = require("validate.js");
    let texts = require('../config/lang')(lang).editList

    let constraints = {

        fieldName : {
            presence : {
                message : texts.missingFieldName,
                allowEmpty: false
            },
            length : {
                message : texts.sizeFieldName,
                minimum: 1,
                maximum: 40
            }
        },

        scoreMax : {
            numericality: {
                message: texts.formatScoreMax,
                onlyInteger: true,
                greaterThan: 0
              }
        },

        type : {
            presence: {
                message: texts.missingType,
                allowEmpty: false
              }
        },
    };

    return validate(datas, constraints)
}

export default validateDatas