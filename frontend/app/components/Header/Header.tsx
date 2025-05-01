import React from 'react';
import './Header.css';
import {
  Button,
  Box,
} from '@cloudscape-design/components';
import { FiUser, FiHome, FiSearch, FiPlus, FiMessageCircle } from "react-icons/fi";
import { useNavigate, useLocation } from 'react-router-dom';

const Header = ({ userName = "Tommy Trojan", selectedTab = "Dashboard", hasUnread = true }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const currentPath = location.pathname;

  return (
    <Box className="header-container">
      <div className="header-left">
        <img src="/assets/usc_logo.png" alt="USC Logo" className="usc-logo" />
        <h2 style={{ margin: 0 }}>Study Group Finder</h2>
      </div>

      <div className="header-center">
        {/* TODO: change the onClick navigation once the page is done */}
        <button onClick={() => {navigate('/dashboard')}} className={`header-tab ${currentPath === '/dashboard' ? 'selected' : ''}`}>
          <FiHome /> Dashboard
        </button>

        <button onClick={() => {navigate('/')}} className={`header-tab ${currentPath === '/' ? 'selected' : ''}`}>
          <FiSearch /> Search Groups
        </button>

        <button onClick={() => {navigate('/')}} className={`header-tab ${currentPath === '/create' ? 'selected' : ''}`} >
          <FiPlus /> Create Group
        </button>

        <button onClick={() => {navigate('/inbox');}} className={`header-tab ${currentPath === '/inbox' ? 'selected' : ''}`} >
          <FiMessageCircle /> Inbox
          {/* red dot appears if there's unread messages */}
          {hasUnread && <span className="unread-dot" />} 
        </button>
      </div>

      <div className="header-right">
        <FiUser />
        <span>Hi, {userName}</span>
        {/* TODO: actually log user out once the backend is done */}
        <Button onClick={() => alert("Logging out...")}>
          Log out
        </Button>
      </div>
    </Box>
  );
};

export default Header;