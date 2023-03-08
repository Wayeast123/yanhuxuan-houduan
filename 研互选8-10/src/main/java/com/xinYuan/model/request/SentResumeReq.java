package com.xinYuan.model.request;

public class SentResumeReq
{
    private Integer sendId;

//    @NotNull(message = "name不能为空")
    private String sendName;

//    @NotNull(message = "schoolName不能为空")
    private String sendSchool;

//    @NotNull(message = "majorName不能为空")
    private String sendMajor;

//    @NotNull(message = "telephone不能为空")
    private String sendTelephone;

//    @NotNull(message = "content不能为空")
    private String sendContent;

//    @NotNull(message = "objectId不能为空")
    private Integer receiveId;

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
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

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }
}
