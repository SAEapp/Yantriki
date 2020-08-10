package com.example.quizapp;

public class setslist {

    String QuizName, QuizDisc;

    public setslist(String quizName, String quizDisc) {
        QuizName = quizName;
        QuizDisc = quizDisc;
    }

    public setslist() {

    }

    public String getQuizName() {
        return QuizName;
    }

    public void setQuizName(String quizName) {
        QuizName = quizName;
    }

    public String getQuizDisc() {
        return QuizDisc;
    }

    public void setQuizDisc(String quizDisc) {
        QuizDisc = quizDisc;
    }
}
