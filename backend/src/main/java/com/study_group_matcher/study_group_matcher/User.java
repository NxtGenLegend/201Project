package com.study_group_matcher.study_group_matcher;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {
    
    // Original Attributes
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    private String displayName;
    
    @OneToMany(mappedBy = "sender")
    private List<Message> messages = new ArrayList<>();
    
    @OneToMany(mappedBy = "user")
    private List<StudyGroupMembership> studyGroups = new ArrayList<>();
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Inbox inbox;
    
    // Security Addition
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    // Original Methods
    public void joinGroup(StudyGroup group) {
        StudyGroupMembership membership = new StudyGroupMembership(this, group);
        studyGroups.add(membership);
    }
    
    public void leaveGroup(StudyGroup group) {
        studyGroups.removeIf(m -> m.getStudyGroup().equals(group));
    }
    
    public void sendMessage(Message message) {
        message.setSender(this);
        messages.add(message);
    }
    
    // Security Methods (implements UserDetails)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
