package com.example.quizapp;

public class LBUsers2 {
    public LBUsers2(String fName, int level2score) {
        this.fName = fName;
        this.level2score = level2score;
    }

    public int getLevel2score() {
        return level2score;
    }

    public void setLevel2score(int level2score) {
        this.level2score = level2score;
    }

    String fName, phone, email;
    int level2score;

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



    public LBUsers2() {
    }

}
