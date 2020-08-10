package com.example.quizapp;

public class LBUsers {

    String fName, phone, email;
    int full_score;

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

    public int getFull_score() {
        return full_score;
    }

    public void setFull_score(int full_score) {
        this.full_score = full_score;
    }

    public LBUsers(String fName, int full_score) {
        this.fName = fName;
        this.full_score = full_score;
    }

    public LBUsers() {
    }

}
