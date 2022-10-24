import React, { useContext } from 'react';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import Container from 'react-bootstrap/Container';
import { NavLink } from "react-router-dom";
import UserContext from '../../services/UserContextProvider';

import { getCurrentLang} from '../../services/LinkService'

let langAcron = getCurrentLang()
let texts = require('../../config/lang')(langAcron).navbar

const NavBar = ({type}) => {

  const [userCtx, setUserCtx] = useContext(UserContext);

  let isLoggedIn = Object.keys(userCtx).length !== 0

  let activeClassName = "active fw-bold";
  const activeStyleFunction = ({ isActive }) => isActive ? 'nav-link ' + activeClassName : 'nav-link'

  return (
    <Navbar collapseOnSelect expand="lg" className="justify-content-center mb-4 shadow-lg" bg="primary" variant="dark">
      <Container>
        <Navbar.Brand href="/">
          <i className="text-white fas fa-ranking-star mb-2"></i>
        </Navbar.Brand>
        <Navbar.Brand href="/">{texts.title}</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">

          { !isLoggedIn && 
            <Nav className="m-auto abs-center-x">
              <NavLink className={activeStyleFunction} end
                to="/">{texts.home} 
              </NavLink>
              <Navbar.Text>
                -
              </Navbar.Text>
              <NavLink className={activeStyleFunction}
                to="inscription">{texts.register}
              </NavLink>
              <Navbar.Text>
                -
              </Navbar.Text>
              <NavLink className={activeStyleFunction}
                to="connexion">{texts.connection}
              </NavLink>
            </Nav>
          }

          { isLoggedIn && 
            <Nav className=" m-auto abs-center-x">
              <NavLink className={activeStyleFunction} end
                to="mesclassements">{texts.reviews} 
              </NavLink>
              <Navbar.Text>
                -
              </Navbar.Text>
              <NavLink className={activeStyleFunction}
                to="gestionclassements">{texts.reviewsManager}
              </NavLink>
              <Navbar.Text>
                -
              </Navbar.Text>
              <NavLink className={activeStyleFunction}
                to="deconnexion">{texts.logoff} [{userCtx.username}] <i className="fa-solid fa-power-off"></i>
              </NavLink>
            </Nav>
          } 



        </Navbar.Collapse>
      </Container>
    </Navbar>
  )

}

export default NavBar;