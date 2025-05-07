import React from 'react';
import './Header.css';
import {
  Button,
  Box,
} from '@cloudscape-design/components';
import { FiUser, FiHome, FiSearch, FiPlus, FiMessageCircle } from "react-icons/fi";
import { useNavigate, useLocation } from 'react-router-dom';

const Header = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const currentPath = location.pathname;

  const email = localStorage.getItem('email') || '';
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';

  return (
    <Box className="header-container">
      <div className="header-left">
        <img src="/assets/usc_logo.png" alt="USC Logo" className="usc-logo" />
        <h2 style={{ margin: 0 }}>Study Group Finder</h2>
      </div>

      <div className="header-center">
        <button onClick={() => {navigate('/')}} className={`header-tab ${currentPath === '/' ? 'selected' : ''}`}>
          <FiSearch /> Search Groups
        </button>
        {
          isLoggedIn ? (
            <>
              <button onClick={() => {navigate('/dashboard')}} className={`header-tab ${currentPath === '/dashboard' ? 'selected' : ''}`}>
                <FiHome /> Dashboard
              </button>
              <button onClick={() => {navigate('/createStudyGroup')}} className={`header-tab ${currentPath === '/createStudyGroup' ? 'selected' : ''}`} >
                <FiPlus /> Create Group
              </button>
              <button onClick={() => {navigate('/inbox');}} className={`header-tab ${currentPath === '/inbox' ? 'selected' : ''}`} >
                <FiMessageCircle /> Inbox
              </button>
          </>
          ) : (
            <></>
          )
    }
      </div>

      <div className="header-right">
        {
          isLoggedIn ? (
            <>
              <FiUser />
              <span>Hi, {email}</span>
              <Button onClick={() => {localStorage.setItem('email', ""); localStorage.setItem('isLoggedIn', "false"); navigate("/");}}>
                Log out
              </Button>
            </>
          ) : (
            <>
              <Button onClick={() => navigate('/login')}>
                Log in
              </Button>
              <Button onClick={() => navigate('/register')}>
                Register
              </Button>
            </>
          )
        }
      </div>
    </Box>
  );
};

export default Header;