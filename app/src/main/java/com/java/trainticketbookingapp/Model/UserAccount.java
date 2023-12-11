package com.java.trainticketbookingapp.Model;

public class UserAccount {
    private String userId;
    private String email;
    private String password;
    private String language; // New field to store language preference

    public UserAccount(String userId, String email, String password, String language) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.language = language;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
