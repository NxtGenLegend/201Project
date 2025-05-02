package com.study_group_matcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // getters and setters automatically defined
@NoArgsConstructor // default constructor
@AllArgsConstructor // constructor taking all fields as arguments; use when loading from the database
public class User {
    private int userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    // constructor for creating a new user before inserting into the database (during registration)
    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}