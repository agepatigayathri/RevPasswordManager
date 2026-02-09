package com.revature.model;


public class User {
    private int userId;
    private String name;
    private String email;
    private String masterPassword;

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMasterPassword() { return masterPassword; }
    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }
}

