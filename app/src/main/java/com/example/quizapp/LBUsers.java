package com.example.quizapp;

public class LBUsers {

    String fName, phone, email;
    int level1score;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LBUsers(String fName, int level1score) {
        this.fName = fName;
        this.level1score = level1score;
    }

    public int getLevel1score() {
        return level1score;
    }

    public void setLevel1score(int level1score) {
        this.level1score = level1score;
    }

    public LBUsers() {
    }

}
