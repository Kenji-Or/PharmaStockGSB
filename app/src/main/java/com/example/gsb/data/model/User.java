package com.example.gsb.data.model;

public class User {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int role;

    public User(Long id, String firstName, String lastName, String email, int role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

//    public String getPassword() {
//        return password;
//    }

    public int getRole() {
        return role;
    }
}
