package com.xinYuan.model.request;

import javax.validation.constraints.NotNull;

public class PersonalResumeReq {

    private Integer userId;

    private String userName;

    private String schoolName;

    private String majorName;

    private String telephone;

    private String content;

    private Integer identity;

    private String targetSchool;

    private String targetMajor;

    private Integer score;

    private Integer testScore;

    public String getTargetSchool() {
        return targetSchool;
    }

    public void setTargetSchool(String targetSchool) {
        this.targetSchool = targetSchool;
    }

    public String getTargetMajor() {
        return targetMajor;
    }

    public void setTargetMajor(String targetMajor) {
        this.targetMajor = targetMajor;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public Integer getTestScore() {
        return testScore;
    }

    public void setTestScore(Integer testScore) {
        this.testScore = testScore;
    }

    @Override
    public String toString() {
        return "PersonalResumeReq{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", majorName='" + majorName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", content='" + content + '\'' +
                ", identity=" + identity +
                ", targetSchool='" + targetSchool + '\'' +
                ", targetMajor='" + targetMajor + '\'' +
                ", score=" + score +
                ", testScore=" + testScore +
                '}';
    }
}
