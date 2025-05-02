package com.study_group_matcher.model;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import com.study_group_matcher.model.Message;
import com.study_group_matcher.model.StudyGroup;
import com.study_group_matcher.model.Inbox;

public class User {
    public User(long id, String p, String f, String l, String d){
        user_id = id;
        password = p;
        firstName = f;
        lastName = l;
        displayName = d;
        messages = new ArrayList<>();
        studyGroups = new ArrayList<>();
        inbox = new Inbox();
        lastLoginTime = LocalDateTime.now();
    }
    public User(){

    }
    public Long getUser_id(){
        return user_id;

    }
    public void setUser_id(long id){
        user_id = id;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String p){
        password = p;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String f){
        firstName = f;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String l){
        lastName = l;
    }
    public String getDisplayName(){
        return displayName;
    }
    public void setDisplayName(String d){
        displayName = d;
    }
    public List<Message> getMessages(){
        return messages;
    }
    public List<Message> setMessages(List<Message> m){
        messages = m;
    }
    public List<StudyGroup> getStudyGroups(){
        return studyGroups;
    }
    public Inbox getInbox(){
        return inbox;
    }
    public Inbox setInbox(Inbox i){
        inbox = i;
    }
    public LocalDateTime getLastLoginTime(){
        return lastLoginTime;
    }
    public void setLastLoginTime(LocalDateTime t){
        lastLoginTime = t;
    }

    private
    Long user_id;
    LocalDateTime lastLoginTime;
    String password;
    String firstName;
    String lastName;
    String displayName;
    List<Message> messages;
    List<StudyGroup> studyGroups;
    Inbox inbox;

}
