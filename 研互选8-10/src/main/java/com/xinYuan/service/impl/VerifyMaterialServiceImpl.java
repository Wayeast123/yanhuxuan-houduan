package com.xinYuan.service.impl;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.dao.UserMapper;
import com.xinYuan.model.dao.VerifyMaterialMapper;
import com.xinYuan.model.pojo.InformationRelease;
import com.xinYuan.model.pojo.VerifyMaterial;
import com.xinYuan.model.request.InsertMaterialReq;
import com.xinYuan.service.VerifyMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerifyMaterialServiceImpl implements VerifyMaterialService
{
    @Autowired
    VerifyMaterialMapper verifyMaterialMapper;

    /**
     *  1.用户验证身份材料填写
     */
    @Override
    public void verifyIdentity(InsertMaterialReq insertMaterialReq)
    {

        VerifyMaterial verifyMaterial = new VerifyMaterial();
        BeanUtils.copyProperties(insertMaterialReq, verifyMaterial);

        //1.1 判断该用户是否已经提交审核资料，并且在审核中
        VerifyMaterial verifyMaterialOld = verifyMaterialMapper.selectByUserId(verifyMaterial.getUserId());
        if (verifyMaterialOld != null)
        {
            throw new XinYuanException(XinYuanExceptionEnum.USERNAME_REPEAT);
        }

        //1.2 将认证材料插入到数据库
        int count = verifyMaterialMapper.insertSelective(verifyMaterial);
        if (count == 0)
        {
            throw new XinYuanException(XinYuanExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     *  2.用户上传身份证正面
     */
    public void uploadIdCardFront(int userId, String image) throws XinYuanException
    {
        //2.1 获取这一用户的最新一次发布数据id
        VerifyMaterial verifyMaterialOld = verifyMaterialMapper.selectByUserId(userId);

        //2.2 准备数据
        VerifyMaterial verifyMaterial = new VerifyMaterial();
        verifyMaterial.setId(verifyMaterialOld.getId());
        verifyMaterial.setIdCardFront(image);

        int updateCount = verifyMaterialMapper.updateByPrimaryKeySelective(verifyMaterial);
        if (updateCount > 1) {  //更新成功的用户应当只有1个
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     *  3.用户上传身份证背面
     */
    public void uploadIdCardBack(int userId, String image) throws XinYuanException
    {
        //3.1 获取这一用户的最新一次发布数据id
        VerifyMaterial verifyMaterialOld = verifyMaterialMapper.selectByUserId(userId);

        //3.2 准备数据
        VerifyMaterial verifyMaterial = new VerifyMaterial();
        verifyMaterial.setId(verifyMaterialOld.getId());
        verifyMaterial.setIdCardBack(image);

        int updateCount = verifyMaterialMapper.updateByPrimaryKeySelective(verifyMaterial);
        if (updateCount > 1) {  //更新成功的用户应当只有1个
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     *  4.用户上传其它证明
     */
    public void uploadCertificate(int userId, String image) throws XinYuanException
    {
        //4.1 获取这一用户的最新一次发布数据id
        VerifyMaterial verifyMaterialOld = verifyMaterialMapper.selectByUserId(userId);

        //4.2 准备数据
        VerifyMaterial verifyMaterial = new VerifyMaterial();
        verifyMaterial.setId(verifyMaterialOld.getId());
        verifyMaterial.setCertificate(image);

        int updateCount = verifyMaterialMapper.updateByPrimaryKeySelective(verifyMaterial);
        if (updateCount > 1) {  //更新成功的用户应当只有1个
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     *  5.获取审核信息（管理员）
     */
    public List<VerifyMaterial> auditInfo(Integer identity) throws XinYuanException
    {
        return verifyMaterialMapper.auditInfo(identity);
    }
}
