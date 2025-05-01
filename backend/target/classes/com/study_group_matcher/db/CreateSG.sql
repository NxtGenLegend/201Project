CREATE DATABASE IF NOT EXISTS STUDY_GROUP_MATCHER;
USE STUDY_GROUP_MATCHER;

CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(225) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    last_login_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE StudyGroup (
    study_group_id INT PRIMARY KEY AUTO_INCREMENT,
    admin_id INT NOT NULL,
    privacy ENUM('PUBLIC', 'PRIVATE') NOT NULL,
    max_members INT NOT NULL,
    creation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES Users(user_id)
);

CREATE TABLE StudyGroupMembers (
    study_group_id INT NOT NULL,
    user_id INT NOT NULL,
    group_role ENUM('ADMIN', 'MEMBER') NOT NULL,
    joined_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (study_group_id, user_id),
    FOREIGN KEY (study_group_id) REFERENCES StudyGroup(study_group_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Message (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    recipient_id INT NOT NULL,
    message_contents TEXT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES Users(user_id),
    FOREIGN KEY (recipient_id) REFERENCES Users(user_id)
);

CREATE TABLE Inbox (
    inbox_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    sender_id INT NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'DECLINED') NOT NULL,
    notification ENUM('NOTIFIED', 'SEEN'),
    contents TINYTEXT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (sender_id) REFERENCES Users(user_id)
);

CREATE TABLE Invitations (
  invitation_id INT PRIMARY KEY AUTO_INCREMENT,
  study_group_id INT NOT NULL,
  user_id INT NOT NULL,
  status ENUM('PENDING', 'ACCEPTED', 'DECLINED') NOT NULL DEFAULT 'PENDING',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME,
  FOREIGN KEY (study_group_id) REFERENCES StudyGroup(study_group_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
