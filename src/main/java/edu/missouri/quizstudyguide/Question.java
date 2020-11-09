package edu.missouri.quizstudyguide;

import java.util.ArrayList;

//question model, defining what a question object should look like
public class Question {
    private String question;    //the question itself
    private ArrayList<String> answers = new ArrayList<String>();    //its options
    private int correctIndex;   //the index of the correct answer

    //default constructor
    public Question() {
    }

    //question constructor
    public Question(String question, ArrayList<String> answers, int correctIndex) {
        this.question = question;
        this.answers = answers;
        this.correctIndex = correctIndex;
    }

    //getters and setters
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public ArrayList<String> getAnswers() { return answers; }
    public void setAnswers(ArrayList<String> answers) { this.answers = answers; }
    public int getCorrectIndex() { return correctIndex; }
    public void setCorrectIndex(int correctIndex) { this.correctIndex = correctIndex; }
}
