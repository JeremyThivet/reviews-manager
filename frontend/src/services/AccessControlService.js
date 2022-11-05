/**
 * This is the service responsible for AccessControl.
 * Its duties are :
 *  - Perform the login request and store the access and refresh token locally.
 *  - For each request done in the authenticated part of the application (once you're logged in), it will perform the request and then return the result to the caller.
 *      - If the request return an "Unauthorized", then we check if the refresh token is set.
 *          - If set, the AccessControlService ask for a new access token.
 *          - If Refresh token not set or Expired (return a 401) : we will throw an error (and the user might be redirect to the login page by the caller).
 *  - For the logout part, the AccessControl will delete every token from the storage.
 */

 import { getCurrentLang } from './LinkService'
 
 let langAcron = getCurrentLang()
 let texts = require('../config/lang')(langAcron).accesscontrol


/**
 * This function perform the HTTP call to the backend API which needs authentication / authorization.
 * @param {*}
 * @returns 
 */
export async function handleCall(url, httpMethod, data) {

    let result = { errors: false, success: false , needToLoginAgain: false, data: {}}

    let response = await performHttpCall(url, httpMethod, data, getAccessTokenInLocalStorage())

    // If servers returns 401 (UNAUTHORIZED), then we do not have a valid AccessToken, we try to get a new one.
    if(response.status === 401){

        // We try to get a new AccessToken
        let successFullRefresh = await tryToRefreshAccessToken()
        
        if(!successFullRefresh) {
            result.needToLoginAgain = true;
            return result;
        }

        // Call again with the new AccessToken
        response = await performHttpCall(url, httpMethod, data, getAccessTokenInLocalStorage())
    }

    result.success = response.ok
    result.errors = !response.ok

    // If response has content, we add it to the result
    if(response.headers.get("content-length") !== "0"){

        let res = await response.json()
        result.data = res

    }

    return result;
}


/**
  * Login the user through the API that will provide a refresh and an access token.
  * @param {*} data {username : "username", password: "password"}
  * @returns {errors : [], success: boolean}, errors and success are set depending on the response's status.
  */
 export async function loginUser(data) {

    let result = { errors: [], success: false, data: {}}

    const response = await fetch(`/api/login`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
        })
    
    // The server return 200 if OK, or 403 otherwise.
    if(!response.ok){

        result.errors.push(texts.loginError)

    } else {

        result.success = true;
        
        // Extracting Access Token and store it into localStorage
        let accessToken = response.headers.get("authorization").split(' ')[1]
        storeAccessTokenToLocalStorage(accessToken)

        let res = await response.json()
        result.data = res
    }

    return result;
}

export async function logoOutUser() {

    let result = { errors: false, success: false, data: {}}

    const response = await performHttpCall('/api/logout', 'GET', {}, '')
    
    if(!response.ok){

        result.errors = true;
        return result;

    } else {

        result.success = true;
        
        // Deleting session storage
        sessionStorage.clear()
    }

    return result;
}


function storeAccessTokenToLocalStorage(token){
    sessionStorage.setItem("access-token", token)
}

function getAccessTokenInLocalStorage(){
    return sessionStorage.getItem("access-token")
}

/**
 * The functions tries to refresh the access token.
 * @returns True if the AccessToken has been refresh, false otherwise.
 */
async function tryToRefreshAccessToken() {

    // Try to refresh the AccessToken
    const response = await performHttpCall('/api/refreshToken', 'GET', {}, '');

    if(response.ok){

        let accessToken = response.headers.get("authorization").split(' ')[1]
        storeAccessTokenToLocalStorage(accessToken)
        return true;

    } else {

        // We cannot get an accesstoken.
        return false;
    }

}

/**
 * Perform an HTTP call to the url, using the httpMethod and embedding the given data in JSON. It add the given Authorization token if it exists.
 * @param {*} url 
 * @param {*} httpMethod 
 * @param {*} data 
 * @param {*} at AccessToken for the authenticated request if needed.
 * @returns 
 */
async function performHttpCall(url, httpMethod, data, at){

    let parameters = {
        method: httpMethod,
        headers: {}
    }

    if(Object.keys(data).length !== 0){
        parameters.headers['Content-Type'] = 'application/json'
        parameters.body = JSON.stringify(data);
    }

    if(at){
        parameters.headers['Authorization'] = 'Bearer ' + at
    }

    const response = await fetch(url, parameters)

    return response;

}