import { useEffect, useContext } from "react";
import { logoOutUser } from '../../services/AccessControlService'
import UserContext from "../../services/UserContextProvider";



const Logout = () => {

    const [userCtx, setUserCtx] = useContext(UserContext);

    useEffect(() => {
        (async function(){
            
          // Clearing Session Storage Containing the AccesToken and Clearing the RefreshToken stored in cookie by calling the rest API  
          const res = await logoOutUser();
            
          if(res.success){
            // Clearing Context and LocalStorage
            setUserCtx({})
          } else {
            // TODO handle error in logout.
          }

        })()
      }, [])

  }


export default Logout