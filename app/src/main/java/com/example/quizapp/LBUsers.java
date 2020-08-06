package com.example.quizapp;

public class LBUsers {

    String fName, total_score,phone,email;

    public LBUsers() {}
    public LBUsers(String fName, String total_score) {
        this.fName = fName;
        this.total_score = total_score;
    }

    public String getfName() {
        return fName;
    }

    public String getTotal_score() {
        return total_score;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

}
