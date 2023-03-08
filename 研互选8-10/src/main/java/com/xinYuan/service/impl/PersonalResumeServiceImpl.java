package com.xinYuan.service.impl;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.dao.PersonalResumeMapper;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.request.PersonalResumeReq;
import com.xinYuan.model.vo.PersonalResumeVO;
import com.xinYuan.service.PersonalResumeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalResumeServiceImpl implements PersonalResumeService
{
    @Autowired
    PersonalResumeMapper personalResumeMapper;

    /**
     *  1.创建个人简历
     */
    @Override
    public void create(PersonalResumeReq personalResumeReq) throws XinYuanException
    {
        //1.1 准备个人简历数据
        PersonalResume personalResume = new PersonalResume();
        BeanUtils.copyProperties(personalResumeReq, personalResume);

        if (personalResume.getIdentity() == 2)
        {
            personalResume.setFuzzySearch(personalResume.getUserName() + personalResume.getSchoolName() +
                    personalResume.getMajorName() + personalResume.getTelephone() + personalResume.getContent()
                    + personalResume.getScore());
        }else
        {
            personalResume.setFuzzySearch(personalResume.getUserName() + personalResume.getSchoolName() +
                    personalResume.getMajorName() + personalResume.getTelephone() + personalResume.getContent()
                    + personalResume.getTargetSchool() + personalResume.getTargetMajor());
        }

        //1.2 将个人简历插入到数据库
        int count = personalResumeMapper.insertSelective(personalResume);
        if (count == 0)
        {
            throw new XinYuanException(XinYuanExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     *  2.更新个人简历
     */
    @Override
    public void updatePerRe(PersonalResumeReq personalResumeReq) throws XinYuanException
    {
        //2.1 准备个人简历数据
        PersonalResume personalResume = new PersonalResume();
        BeanUtils.copyProperties(personalResumeReq, personalResume);

        if (personalResume.getIdentity() == 2)
        {
            personalResume.setFuzzySearch(personalResume.getUserName() + personalResume.getSchoolName() +
                    personalResume.getMajorName() + personalResume.getTelephone() + personalResume.getContent()
                    + personalResume.getScore());
        }else
        {
            personalResume.setFuzzySearch(personalResume.getUserName() + personalResume.getSchoolName() +
                    personalResume.getMajorName() + personalResume.getTelephone() + personalResume.getContent()
                    + personalResume.getTargetSchool() + personalResume.getTargetMajor());
        }

        //2.2 更新个人简历数据到数据库
        int updateCount = personalResumeMapper.updateByPrimaryKeySelective(personalResume);
        if (updateCount > 1)
        {
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     *  3.展示个人简历
     */
    @Override
    public PersonalResume showPersonalResume(Integer userId) throws XinYuanException
    {
        //3.1 获取个人简历
        PersonalResume personalResume = personalResumeMapper.selectByUserId(userId);

        //3.2 判断是否有创建过简历
        if(personalResume == null)
        {
            throw new XinYuanException(XinYuanExceptionEnum.NO_CREATE_RESUME);
        }

        return personalResume;
    }

    /**
     *  4.个人简历列表
     */
    @Override
    public List<PersonalResumeVO> showPersonalResumeList(Integer identity, Integer userId) throws XinYuanException
    {
        if (identity == 1)
        {
            return personalResumeMapper.selectByAllTutor(2);
        }
        //导师 看 准研究生
        else if (identity == 2)
        {
            PersonalResume personalResume = personalResumeMapper.selectByUserId(userId);
            String schoolName = personalResume.getSchoolName();
            return personalResumeMapper.selectStudent(3, schoolName);
        }
        //准研究生 看 导师
        else
        {
            PersonalResume personalResume = personalResumeMapper.selectByUserId(userId);
            String targetSchool = personalResume.getTargetSchool();
            return personalResumeMapper.selectTutor(2, targetSchool);
        }

    }

    /**
     *  5.个人简历筛选（登录）
     */
    @Override
    public List<PersonalResumeVO> selectPersonalResume(String schoolName, String majorName, Integer identity)
            throws XinYuanException
    {
        //5.1 导师的筛选有不同
        if (identity == 3)
        {
            //5.1.1 筛选条件都为空，报错
            if (schoolName.equals("") && majorName.equals(""))
            {
                throw new XinYuanException(XinYuanExceptionEnum.SCREEN_NOT_NULL);
            }
            //5.1.2 筛选学校
            else if (majorName.equals(""))
            {
                return personalResumeMapper.selectByTargetSchool(schoolName, identity);
            }
            //5.1.3 筛选专业
            else if (schoolName.equals(""))
            {
                return personalResumeMapper.selectByTargetMajor(majorName, identity);
            }
            //5.1.4 筛选学校 + 专业
            return personalResumeMapper.selectByTargetSchoolAndTargetMajor(schoolName, majorName, identity);
        }

        //5.2 筛选条件都为空，报错
        if (schoolName.equals("") && majorName.equals(""))
        {
            throw new XinYuanException(XinYuanExceptionEnum.SCREEN_NOT_NULL);
        }
        //5.3 筛选学校
        else if (majorName.equals(""))
        {
            return personalResumeMapper.selectBySchoolName(schoolName, identity);
        }
        //5.4 筛选专业
        else if (schoolName.equals(""))
        {
            return personalResumeMapper.selectByMajorName(majorName, identity);
        }
        //5.5 筛选学校 + 专业
        return personalResumeMapper.selectBySchoolNameAndMajorName(schoolName, majorName, identity);
    }

    /**
     *  6.个人简历筛选（未登录）
     */
    @Override
    public List<PersonalResumeVO> selectPersonalResumeLogout(String schoolName, String majorName)
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
            return personalResumeMapper.selectBySchoolNameLogout(schoolName);
        }
        //2.3 筛选专业
        else if (schoolName.equals(""))
        {
            return personalResumeMapper.selectByMajorNameLogout(majorName);
        }
        //2.4 筛选学校 + 专业
        return personalResumeMapper.selectBySchoolNameAndMajorNameLogout(schoolName, majorName);
    }

    /**
     *  7.个人简历模糊搜索
     */
    @Override
    public List<PersonalResumeVO> personalResumeFuzzySearch(String keyWord, Integer identity, Integer userId)
            throws XinYuanException
    {
        if (identity == 1)
        {
            return personalResumeMapper.FuzzySearchByAllTutor(keyWord, 2);
        }
        //导师 看 准研究生
        else if (identity == 2)
        {
            PersonalResume personalResume = personalResumeMapper.selectByUserId(userId);
            String schoolName = personalResume.getSchoolName();
            return personalResumeMapper.FuzzySearchStudent(keyWord, 3, schoolName);
        }
        //准研究生 看 导师
        else
        {
            PersonalResume personalResume = personalResumeMapper.selectByUserId(userId);
            String targetSchool = personalResume.getTargetSchool();
            return personalResumeMapper.FuzzySearchTutor(keyWord, 2, targetSchool);
        }

    }
}
