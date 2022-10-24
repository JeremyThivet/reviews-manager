function loadLang(langP){
    let lang = require('./fr')
    try{
        return require('./' + langP)
    } catch (err){
        return lang
    }
}

module.exports = loadLang