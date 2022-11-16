import React, { useEffect, useState, useContext } from "react";
import SubscriptionPage from "./components/GuestContent/SubscriptionPage";
import ConnectionForm from "./components/GuestContent/ConnectionForm";
import Footer from "./components/GeneralContent/Footer";
import NavBar from "./components/GeneralContent/NavBar";
import FirstUserPage from "./components/GuestContent/FirstUserPage";
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet} from "react-router-dom";
import { getUsersCount } from "./services/UserService"
import UserContext, { UserProvider } from "./services/UserContextProvider";
import ListsPage from "./components/UserContent/ListsPage";
import ListsManagerPage from "./components/UserContent/ListsManagerPage";
import Logout from "./components/UserContent/Logout";
import './app.scss';
import ListEditPage from "./components/UserContent/ListEditPage";
import ListViewPage from "./components/UserContent/ListViewPage";


/**
 * this is a NestedRoutes Layout used to redirect user to the first admin subscription form if he is the app first user.
 * @returns If this is the first user, we redirect him to the first sub form, otherwise, we return the children.
 */
 const InitializationLayout = () => {

  const [usersCount, setUsersCount] = useState(-1);
  const [userCtx, setUserCtx] = useContext(UserContext);

  useEffect(() => {
    (async function(){
      const val = await getUsersCount();
      setUsersCount(val)
    })()
  }, [])

  const isFirstUser = usersCount === 0;

  if (isFirstUser) {
    return <Navigate to="/firstuser" />;
  }

   // If user is already logged in, we redirect him to the home page of authenticated users.
   if(Object.keys(userCtx).length !== 0){
    return <Navigate to="/mesclassements" />;
  }

  return (
    <>
        <NavBar />
        <Outlet />
    </>
  )
}


/**
 * this is a NestedRoutes Layout used to redirect user to the login if page if he is not authenticated.
 * @returns If not user in the context, redirect to the login page, others wise, display childrens.
 */
 const AuthenticatedLayout = () => {
  const [userCtx, setUserCtx] = useContext(UserContext)
   if(userCtx.needToLoginAgain){
     return <Navigate to="/deconnexion" />;
   }

   if(Object.keys(userCtx).length === 0){
    return <Navigate to="/connexion" />;
  }

  return (
    <>
        <NavBar />
        <Outlet />
    </>
  )
}


function InnerApp() {

  return (
    <>
      <Router>
        <Routes>
              <Route element={<InitializationLayout />}>
                <Route path="/" />
                <Route exact path={"/inscription"} element={<SubscriptionPage />} />
                <Route exact path={"/connexion"} element={<ConnectionForm />
                } />
              </Route>

              <Route element={<AuthenticatedLayout />}>
                <Route exact path={"/mesclassements"} element={<ListsPage />} />
                <Route exact path={"/gestionclassements"} element={<ListsManagerPage />} />
                <Route exact path={"/editerclassement/:listId"} element={<ListEditPage />} />
                <Route exact path={"/consulterclassement/:listId"} element={<ListViewPage />} />
              </Route>

              <Route exact path={"/firstuser"} element={<FirstUserPage />} />
              <Route exact path={"/deconnexion"} element={<Logout />} />

              <Route
                path="*"
                element={<Navigate to="/" replace />}
              />
        </Routes>
        <Footer></Footer>
      </Router>
    </>
  )

}

function App() {
  return (
    <UserProvider>
      <InnerApp />
    </UserProvider>
  );
}

export default App; 
