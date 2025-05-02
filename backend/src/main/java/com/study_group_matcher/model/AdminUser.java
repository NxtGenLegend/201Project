package com.study_group_matcher.model;

import java.util.ArrayList;
import java.util.List;


public class AdminUser extends User {
    public AdminUser(){
        super();
        groupsCreated = new ArrayList<StudyGroup>();
        groupIDSCreated = new ArrayList<Integer>();
    }
    public AdminUser(int userId, String username, String password, String firstName, String lastName, List<Integer> groupIdsCreated) {
        super(userId, password, firstName, lastName, username);
        groupsCreated = new ArrayList<StudyGroup>();
        groupIDSCreated = groupIdsCreated;
    }
    public AdminUser(String username, String password, String firstName, String lastName) {
        super(username, password, firstName, lastName);
        groupsCreated = new ArrayList<StudyGroup>();
        groupIDSCreated = new ArrayList<Integer>();
    }
    public List<StudyGroup> getGroupsCreated(){
        return groupsCreated;
    }
    public void setGroupsCreated(List<StudyGroup> gc){
        groupsCreated = gc;
    }
    public List<Integer> getGroupIDSCreated(){
        return groupIDSCreated;
    }
    public void setGroupIDSCreated(List<Integer> ids){
        groupIDSCreated = ids;
    }
    public void addGroupId(int groupId) {
        groupIDSCreated.add(groupId);
    }
    public void removeGroupId(int groupId) {
        groupIDSCreated.remove(groupId);
    }
    public void addGroup(StudyGroup group){
        groupsCreated.add(group);
    }
    public void removeGroup(StudyGroup group){
        groupsCreated.remove(group);
    }
    public boolean hasCreatedGroup(int groupId) {
        for(int i=0; i < groupIDSCreated.size(); i++){
            if(groupIDSCreated.get(i) == groupId){
                return true;
            }
        }
        return false;
    }
    private 
    List<StudyGroup> groupsCreated;
    List<Integer> groupIDSCreated;



}
