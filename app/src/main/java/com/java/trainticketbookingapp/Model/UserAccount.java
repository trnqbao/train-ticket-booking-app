package com.java.trainticketbookingapp.Model;

public class UserAccount {
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userLanguage;
    private int userPoint;

    public UserAccount() {
    }

    public UserAccount(String userName, String userPhone) {
        this.userName = userName;
        this.userPhone = userPhone;
    }

    public UserAccount(String userName, String userEmail, String userPhone) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }

    public UserAccount(String userName, String userEmail, String userPhone, String userLanguage, int userPoint) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userLanguage = userLanguage;
        this.userPoint = userPoint;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }
}
