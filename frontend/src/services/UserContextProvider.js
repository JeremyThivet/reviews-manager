import React, { useState, useEffect } from 'react';

let initialState = {}

let UserContext = React.createContext();

function UserProvider({ children }) {
    const [user, setUser] = useState(() => getLocalStorage("user", initialState));

    useEffect(() => {
        setLocalStorage("user", user);
    }, [user]);

    return (
        <UserContext.Provider
            value={[user, setUser]}
        >
        {children}
        </UserContext.Provider>
    );
}
  
function setLocalStorage(key, value) {
    try {
        if(value === {}){
            localStorage.clear();
            return;
        }
        localStorage.setItem(key, JSON.stringify(value));
    } catch (e) {
        // TODO catch possible errors:
        // https://developer.mozilla.org/en-US/docs/Web/API/Web_Storage_API/Using_the_Web_Storage_API
    }
}
  
function getLocalStorage(key, initialValue) {
    try {
      const value = localStorage.getItem(key);
      return value ? JSON.parse(value) : initialValue;
    } catch (e) {
      // if error, return initial value
      return initialValue;
    }
}

export default UserContext;

export { UserProvider };