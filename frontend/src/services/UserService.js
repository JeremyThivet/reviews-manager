/**
 * This is the service for the users elements. It peforms calls to the Spring Boot API backend.
 */

 import { getCurrentLang } from '../services/LinkService'
 
 let langAcron = getCurrentLang()
 let texts = require('../config/lang')(langAcron).register


  export async function submitUser(data) {

    const response = await fetch(`/api/users`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
      })

    if(!response.ok){

      let errors = []

      // Unique constraint violated
      if(response.status === 460){
        errors.push(texts.usernameAlreadyExists)

      } else {
        errors.push(texts.generalError)
      }

      // If response has content, we add it to the errors
      if(response.headers.get("content-length") !== "0"){
        let res = await response.json();
        for(const key in res){
          errors.push(res[key])
        }
      }
    
      return {errors};

    } else {
      let res = await response.json();
      res.success = "OK";
      return res;
    }
  }


export async function getUsersCount() {

  const response = await fetch(`/api/users/count`, {
      method: 'GET'
    })

  if(response.ok){
    return await response.json();
  } else {
    throw new Error(response.status);
  }
}