package com.test.onlinetest.model;

import java.util.List;

/**
 * Created by mahfuz on 9/9/18.
 */

public class Test {
    private String id;
    private String title;
    private int totalMarks;
    private double estimateTime;
    private List<Question> questions;

    public Test() {
    }

    public Test(String id, String title, int totalMarks, double estimateTime, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.totalMarks = totalMarks;
        this.estimateTime = estimateTime;
        this.questions = questions;
    }

    public double getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(double estimateTime) {
        this.estimateTime = estimateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
