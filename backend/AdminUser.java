package com.study_group_matcher.study_group_matcher;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class AdminUser extends User {
    
    @OneToMany(mappedBy = "admin")
    private List<StudyGroup> groupsCreated = new ArrayList<>();
    
    public AdminUser() {
        this.setRole(Role.ADMIN);
    }
    
    // Original Admin Methods
    public StudyGroup createGroup(String name, String course) {
        StudyGroup group = new StudyGroup(name, course, this);
        groupsCreated.add(group);
        return group;
    }
    
    public void addToGroup(User user, StudyGroup group) {
        if (groupsCreated.contains(group)) {
            user.joinGroup(group);
        }
    }
    
    public void removeFromGroup(User user, StudyGroup group) {
        if (groupsCreated.contains(group)) {
            user.leaveGroup(group);
        }
    }
}
