package com.xinYuan.model.pojo;

import java.util.Date;

public class ResumeSend {
    private Integer id;

    private Integer sendId;

    private String sendName;

    private String sendSchool;

    private String sendMajor;

    private String sendTelephone;

    private String sendContent;

    private Integer receiveId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName == null ? null : sendName.trim();
    }

    public String getSendSchool() {
        return sendSchool;
    }

    public void setSendSchool(String sendSchool) {
        this.sendSchool = sendSchool == null ? null : sendSchool.trim();
    }

    public String getSendMajor() {
        return sendMajor;
    }

    public void setSendMajor(String sendMajor) {
        this.sendMajor = sendMajor == null ? null : sendMajor.trim();
    }

    public String getSendTelephone() {
        return sendTelephone;
    }

    public void setSendTelephone(String sendTelephone) {
        this.sendTelephone = sendTelephone == null ? null : sendTelephone.trim();
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent == null ? null : sendContent.trim();
    }

    public Integer getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Integer receiveId) {
        this.receiveId = receiveId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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