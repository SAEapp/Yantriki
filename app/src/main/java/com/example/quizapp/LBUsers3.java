package com.example.quizapp;

public class LBUsers3 {

    String fName, phone, email;
    int level3score;

    public LBUsers3(String fName, int level3score) {
        this.fName = fName;
        this.level3score = level3score;
    }

    public int getLevel3score() {
        return level3score;
    }

    public void setLevel3score(int level3score) {
        this.level3score = level3score;
    }

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



    public LBUsers3() {
    }

}
