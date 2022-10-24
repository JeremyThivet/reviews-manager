/**
 * See https://validatejs.org/ - Very easy to use
 */
function validateDatas(datas, lang){

    let validate = require("validate.js");
    let texts = require('../config/lang')(lang).register

    let constraints = {

        username : {
            presence : {
                message : texts.missingUsername,
                allowEmpty: false
            },
            length : {
                message : texts.sizeUsername,
                minimum: 2,
                maximum: 14
            },
            format: {
                message : texts.formatUsername, 
                pattern: '\\w*[a-z]\\w*',
                flags: "i",
              }
        },

        password : {
            presence : {
                message : texts.missingPass,
                allowEmpty: false
            },
            length: {
                minimum: 8,
                message: texts.sizePass
            },
            format: {
                message : texts.formatPass, 
                pattern: "(?=\\w*[0-9])(?=\\w*[a-z]).*",
                flags: "i",
              }
        },

        repass : {
            presence : {
                message : texts.missingRepass,
                allowEmpty: false
            },
            equality : {
                message : texts.equalRepass,
                attribute: "password"
            }
        },
    };

    return validate(datas, constraints)
}

export default validateDatas