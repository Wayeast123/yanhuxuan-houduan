package com.xinYuan.model.pojo;

import java.util.Date;

public class TeacherEvaluation {
    private Integer id;

    private Integer postgraduateId;

    private Integer tutorId;

    private Integer score;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostgraduateId() {
        return postgraduateId;
    }

    public void setPostgraduateId(Integer postgraduateId) {
        this.postgraduateId = postgraduateId;
    }

    public Integer getTutorId() {
        return tutorId;
    }

    public void setTutorId(Integer tutorId) {
        this.tutorId = tutorId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}