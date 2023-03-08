package com.xinYuan.model.request;

import javax.validation.constraints.NotNull;

public class InsertMaterialReq {

    private Integer userId;

//    @NotNull(message = "name不能为空")
    private String userName;

//    @NotNull(message = "sex不能为空")
    private String sex;

//    @NotNull(message = "age不能为空")
    private Integer age;

//    @NotNull(message = "hometown不能为空")
    private String hometown;

//    @NotNull(message = "idCardNumber不能为空")
    private  String idCardNumber;

//    @NotNull(message = "telephone不能为空")
    private String telephone;

//    @NotNull(message = "verifyIdentity不能为空")
    private Integer verifyIdentity;

//    @NotNull(message = "idCardFront不能为空")
    private String idCardFront;

//    @NotNull(message = "idCardBack不能为空")
    private String idCardBack;

//    @NotNull(message = "certificate不能为空")
    private String certificate;

    private Integer status;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getVerifyIdentity() {
        return verifyIdentity;
    }

    public void setVerifyIdentity(Integer verifyIdentity) {
        this.verifyIdentity = verifyIdentity;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
