package com.test.onlinetest.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mahfuz on 9/9/18.
 */

public class Test implements Serializable {
    private String id;
    private String title;
    private int totalMarks;
    private long estimateTime;
    private long createDate;
    private int totalParticipant;
    private List<String> rank;
    private boolean publish;

    public Test() {
    }

    public Test(String id, String title, int totalMarks, long estimateTime, long createDate) {
        this.id = id;
        this.title = title;
        this.totalMarks = totalMarks;
        this.estimateTime = estimateTime;
        this.createDate = createDate;
    }


    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public long getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(long estimateTime) {
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


    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getTotalParticipant() {
        return totalParticipant;
    }

    public void setTotalParticipant(int totalParticipant) {
        this.totalParticipant = totalParticipant;
    }

    public List<String> getRank() {
        return rank;
    }

    public void setRank(List<String> rank) {
        this.rank = rank;
    }
}
