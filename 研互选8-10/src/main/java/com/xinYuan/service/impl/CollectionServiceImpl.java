package com.xinYuan.service.impl;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.dao.CollectionMapper;
import com.xinYuan.model.dao.InformationReleaseMapper;
import com.xinYuan.model.dao.PersonalResumeMapper;
import com.xinYuan.model.pojo.Collection;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.request.PersonalResumeReq;
import com.xinYuan.model.vo.CollectionVO;
import com.xinYuan.service.CollectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService
{
    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    PersonalResumeMapper personalResumeMapper;

    /**
     * 1. 收藏
     */
    @Override
    public void collect(Integer collectId, Integer userId) throws XinYuanException
    {
        //1.1 获取被收藏者个人简历
        PersonalResume personalResume = personalResumeMapper.selectByPrimaryKey(collectId);

        //1.2 准备数据
        Collection collection = new Collection();
        collection.setCollectorId(userId);
        collection.setBeCollectedId(personalResume.getUserId());
        collection.setUserName(personalResume.getUserName());
        collection.setSchoolName(personalResume.getSchoolName());
        collection.setMajorName(personalResume.getMajorName());
        collection.setTelephone(personalResume.getTelephone());
        collection.setContent(personalResume.getContent());
        collection.setTargetSchool(personalResume.getTargetSchool());
        collection.setScore(personalResume.getScore());

        int count = collectionMapper.insertSelective(collection);
        if (count == 0) {
            throw new XinYuanException(XinYuanExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 2. 收藏列表
     */
    @Override
    public List<CollectionVO> collectList(Integer userId) throws XinYuanException
    {
        return collectionMapper.selectByAll(userId);
    }

    /**
     * 3. 取消收藏
     */
    @Override
    public void collectCancel(Integer collectId) throws XinYuanException
    {
        int count = collectionMapper.deleteByPrimaryKey(collectId);
        if (count == 0) {
            throw new XinYuanException(XinYuanExceptionEnum.DELETE_FAILED);
        }
    }
}
