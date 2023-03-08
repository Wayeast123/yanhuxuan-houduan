package com.xinYuan.model.dao;

import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.pojo.VerifyMaterial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerifyMaterialMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VerifyMaterial record);

    int insertSelective(VerifyMaterial record);

    VerifyMaterial selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VerifyMaterial record);

    int updateByPrimaryKey(VerifyMaterial record);

    VerifyMaterial selectByUserId(Integer userId);

    List<VerifyMaterial> auditInfo(Integer identity);

    int deleteByUserId(Integer userId);
}