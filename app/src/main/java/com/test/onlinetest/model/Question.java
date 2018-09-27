package com.test.onlinetest.model;

import java.util.List;

/**
 * Created by mahfuz on 9/9/18.
 */

public class Question {
    private String id;
    private String name;
    private List<String> options;
    private int correctAns;
    private String explanation;

    public Question() {
    }

    public Question(String id, String name, List<String> options, int correctAns, String explanation) {
        this.id = id;
        this.name = name;
        this.options = options;
        this.correctAns = correctAns;
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }
}
