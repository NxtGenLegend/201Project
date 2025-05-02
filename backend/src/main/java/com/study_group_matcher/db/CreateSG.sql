CREATE DATABASE IF NOT EXISTS STUDY_GROUP_MATCHER;
USE STUDY_GROUP_MATCHER;

-- Users table 
CREATE TABLE IF NOT EXISTS Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    last_login_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- StudyGroup table 
CREATE TABLE IF NOT EXISTS StudyGroup (
    study_group_id INT PRIMARY KEY AUTO_INCREMENT,
    admin_id INT NOT NULL,
    group_name VARCHAR(255),
    course VARCHAR(255),
    meeting_time DATETIME,
    meeting_type ENUM('IN_PERSON', 'VIRTUAL') DEFAULT 'IN_PERSON',
    location VARCHAR(255),
    privacy ENUM('PUBLIC', 'PRIVATE') DEFAULT 'PUBLIC',
    max_members INT DEFAULT 50,
    current_member_count INT DEFAULT 0,
    FOREIGN KEY (admin_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- StudyGroupMembers junction table 
CREATE TABLE IF NOT EXISTS StudyGroupMembers (
    study_group_id INT NOT NULL,
    user_id INT NOT NULL,
    group_role ENUM('ADMIN', 'MEMBER') NOT NULL DEFAULT 'MEMBER',
    joined_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (study_group_id, user_id),
    FOREIGN KEY (study_group_id) REFERENCES StudyGroup(study_group_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Direct messages between users
CREATE TABLE IF NOT EXISTS Message (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    recipient_id INT NOT NULL,
    message_body TEXT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Group messages
CREATE TABLE IF NOT EXISTS group_message (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    group_id VARCHAR(255) NOT NULL,
    message_body TEXT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Invitations for study groups
CREATE TABLE IF NOT EXISTS Invitations (
    invitation_id INT PRIMARY KEY AUTO_INCREMENT,
    study_group_id INT NOT NULL,
    user_id INT NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'DECLINED') NOT NULL DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME,
    FOREIGN KEY (study_group_id) REFERENCES StudyGroup(study_group_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Inbox for messages and invitations
CREATE TABLE IF NOT EXISTS Inbox (
    inbox_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    sender_id INT NOT NULL,
    message_id INT,
    invitation_id INT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (message_id) REFERENCES Message(message_id) ON DELETE SET NULL,
    FOREIGN KEY (invitation_id) REFERENCES Invitations(invitation_id) ON DELETE SET NULL
);