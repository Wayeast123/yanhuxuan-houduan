package com.xinYuan.service.impl;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.dao.InformationReleaseMapper;
import com.xinYuan.model.dao.PersonalResumeMapper;
import com.xinYuan.model.pojo.InformationRelease;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.vo.InformationReleaseVO;
import com.xinYuan.service.InformationReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InformationReleaseServiceImpl implements InformationReleaseService
{
    @Autowired
    InformationReleaseMapper informationReleaseMapper;

    @Autowired
    PersonalResumeMapper personalResumeMapper;

    /**
     *  1.发布内容查看
     */
    @Override
    public List<InformationReleaseVO> InformationReleaseList() throws XinYuanException
    {
        List<InformationReleaseVO> informationReleaseVOList = informationReleaseMapper.informationReleaseList();
        return informationReleaseVOList;
    }

    /**
     *  2.筛选指定条件的发布内容
     */
    @Override
    public List<InformationReleaseVO> InformationReleaseScreen(String schoolName, String majorName)
            throws XinYuanException
    {
        //2.1 筛选条件都为空，报错
        if (schoolName.equals("") && majorName.equals(""))
        {
            throw new XinYuanException(XinYuanExceptionEnum.SCREEN_NOT_NULL);
        }
        //2.2 筛选学校
        else if (majorName.equals(""))
        {
            return informationReleaseMapper.selectBySchoolName(schoolName);
        }
        //2.3 筛选专业
        else if (schoolName.equals(""))
        {
            return informationReleaseMapper.selectByMajorName(majorName);
        }
        //2.4 筛选学校 + 专业
        return informationReleaseMapper.selectBySchoolNameAndMajorName(schoolName, majorName);
    }

    /**
     *  3.导师发布消息
     */
    @Override
    public void publish(int userId, String content) throws XinYuanException
    {
        //3.1 根据用户id获取个人简历
        PersonalResume personalResume = personalResumeMapper.selectByUserId(userId);

        //3.2 准备数据
        InformationRelease informationRelease = new InformationRelease();
        informationRelease.setUserId(userId);
        informationRelease.setContent(content);
        informationRelease.setScore(personalResume.getScore());
        informationRelease.setUserName(personalResume.getUserName());
        informationRelease.setSchoolName(personalResume.getSchoolName());
        informationRelease.setMajorName(personalResume.getMajorName());
        informationRelease.setFuzzySearch(personalResume.getUserName() +
                personalResume.getSchoolName() + personalResume.getMajorName() + content + personalResume.getScore());

        int count = informationReleaseMapper.insertSelective(informationRelease); //修改成功的条数，插入返回1(使用insertSelective是因为只给两个字段赋值)
        if (count == 0)
        {
            throw new XinYuanException(XinYuanExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     *  4.导师上传图片
     */
    public void uploadFile(int userId, String image) throws XinYuanException
    {
        //4.1 获取这一用户的最新一次发布数据id
        List<InformationRelease> informationReleaseList = informationReleaseMapper.selectByUserId(userId);
        InformationRelease informationReleaseOld = informationReleaseList.get(0);

        //3.2 准备数据
        InformationRelease informationRelease = new InformationRelease();
        informationRelease.setId(informationReleaseOld.getId());
        informationRelease.setImage(image);

        int updateCount = informationReleaseMapper.updateByPrimaryKeySelective(informationRelease);
        if (updateCount > 1) {  //更新成功的用户应当只有1个
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     *  5.导师发布信息模糊搜索
     */
    public List<InformationReleaseVO> informationReleaseFuzzySearch(String keyWord) throws XinYuanException
    {
        return informationReleaseMapper.informationReleaseFuzzySearch(keyWord);
    }
}
