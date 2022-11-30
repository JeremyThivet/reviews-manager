
export async function getAppVersion() {

  const response = await fetch(`/version`, {
      method: 'GET'
    })

  if(response.ok){
    return await response.text();
  } else {
    throw new Error(response.status);
  }
}