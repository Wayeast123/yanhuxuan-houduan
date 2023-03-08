package com.xinYuan.model.vo;

public class ResumeSendVO
{
    private Integer id;

    private Integer sendId;

    private String sendName;

    private String sendSchool;

    private String sendMajor;

    private String sendTelephone;

    private String sendContent;

    private Integer receiveId;

    private Integer status;

    private String createTime;

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
        this.sendName = sendName;
    }

    public String getSendSchool() {
        return sendSchool;
    }

    public void setSendSchool(String sendSchool) {
        this.sendSchool = sendSchool;
    }

    public String getSendMajor() {
        return sendMajor;
    }

    public void setSendMajor(String sendMajor) {
        this.sendMajor = sendMajor;
    }

    public String getSendTelephone() {
        return sendTelephone;
    }

    public void setSendTelephone(String sendTelephone) {
        this.sendTelephone = sendTelephone;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
