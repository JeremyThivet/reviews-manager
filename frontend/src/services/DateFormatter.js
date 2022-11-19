import dateFormat from "dateformat"

/**
 * 
 * @param {*} date date with ISO format 8601 like yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 */
export function convertToReadableFormat(dateString) {

    let date = Date.parse(dateString);

    return dateFormat(date, "dd/mm/yyyy à HH:mm:ss")
}

/**
 * 
 * @param {*} date date with ISO format 8601 like yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 */
 export function convertToReadableFormatDateOnly(dateString) {

    let date = Date.parse(dateString);

    return dateFormat(date, "dd/mm/yyyy")
}

/**
 * 
 * @param {*} date date that we will contert to ISO format 8601 like yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 */
 export function convertToApiFormat(date) {

    return dateFormat(date, "yyyy-mm-dd'T'HH:MM:ss.lp")
}