package com.study_group_matcher.model;

import java.util.ArrayList;
import java.util.List;


public class AdminUser extends User {
    public AdminUser(){

    }
    public AdminUser(List<StudyGroup> gc){
        groupsCreated = gc;
    }
    public List<StudyGroup> getGroupsCreated(){
        return groupsCreated;
    }
    public void setGroupsCreated(List<StudyGroup> gc){
        groupsCreated = gc;
    }
    private List<StudyGroup> groupsCreated;

}
