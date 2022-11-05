import { useEffect, useContext, useState } from "react";
import { logoOutUser } from '../../services/AccessControlService'
import UserContext from "../../services/UserContextProvider";
import { Navigate } from "react-router-dom";



const Logout = () => {

    const [logoutOk, setLogoutOk] = useState(false)
    const [userCtx, setUserCtx] = useContext(UserContext);

    useEffect(() => {
        (async function(){

          // Clearing Session Storage Containing the AccesToken and Clearing the RefreshToken stored in cookie by calling the rest API  
          const res = await logoOutUser();
            
          if(res.success){
            // Clearing Context and LocalStorage
            setUserCtx({})
            setLogoutOk(true)
          } else {
            // TODO handle error in logout.
          }

        })()
      }, [])

      return (
        <>
          { (logoutOk) && <Navigate to="/connexion" /> }
        </>
      )

  }


export default Logout