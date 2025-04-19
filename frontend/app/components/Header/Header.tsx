import React from 'react';
import './Header.css';
import {
  Button,
  Box,
} from '@cloudscape-design/components';
import { FiUser, FiHome, FiSearch, FiPlus, FiMessageCircle } from "react-icons/fi";

const Header = ({ userName = "Tommy Trojan", selectedTab = "Dashboard", hasUnread = true }) => {
  return (
    <Box className="header-container">
      <div className="header-left">
        <img src="/assets/usc_logo.png" alt="USC Logo" className="usc-logo" />
        <h2 style={{ margin: 0 }}>Study Group Finder</h2>
      </div>

      <div className="header-center">
        {/* TODO: change the onClick navigation once the page is done */}
        <button onClick={() => { }} className={`header-tab ${selectedTab === 'Dashboard' ? 'selected' : ''}`}>
          <FiHome /> Dashboard
        </button>

        <button onClick={() => { }} className={`header-tab ${selectedTab === 'Search Groups' ? 'selected' : ''}`}>
          <FiSearch /> Search Groups
        </button>

        <button onClick={() => { }} className={`header-tab ${selectedTab === 'Create Group' ? 'selected' : ''}`} >
          <FiPlus /> Create Group
        </button>

        <button onClick={() => { }} className={`header-tab ${selectedTab === 'Inbox' ? 'selected' : ''}`} >
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