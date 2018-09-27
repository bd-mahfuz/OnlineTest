package com.test.onlinetest.model;

import java.io.Serializable;

/**
 * Created by mahfuz on 9/25/18.
 */

public class ParticipantDetails implements Serializable{

    private String id;
    private String isFinished;
    private String score;
    private String totalAnswered;
    private String totalQuestion;

    public ParticipantDetails() {
    }

    public ParticipantDetails(String id, String isFinished, String score, String totalAnswered, String totalQuestion) {
        this.id = id;
        this.isFinished = isFinished;
        this.score = score;
        this.totalAnswered = totalAnswered;
        this.totalQuestion = totalQuestion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(String totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(String totalQuestion) {
        this.totalQuestion = totalQuestion;
    }
}
