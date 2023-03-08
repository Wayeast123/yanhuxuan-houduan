package com.xinYuan.service;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.model.pojo.VerifyMaterial;
import com.xinYuan.model.request.InsertMaterialReq;

import java.util.List;

public interface VerifyMaterialService
{
    /* 用户验证身份材料填写 */
    void verifyIdentity(InsertMaterialReq insertMaterialReq) throws XinYuanException;

    /* 用户上传身份证正面 */
    void uploadIdCardFront(int userId, String image) throws XinYuanException;

    /* 用户上传身份证背面 */
    void uploadIdCardBack(int userId, String image) throws XinYuanException;

    /* 用户上传其它证明 */
    void uploadCertificate(int userId, String image) throws XinYuanException;

    /* 获取审核信息（管理员） */
    List<VerifyMaterial> auditInfo(Integer identity) throws XinYuanException;
}
