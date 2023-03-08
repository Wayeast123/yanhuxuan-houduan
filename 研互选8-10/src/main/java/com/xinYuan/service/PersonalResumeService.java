package com.xinYuan.service;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.request.PersonalResumeReq;
import com.xinYuan.model.vo.PersonalResumeVO;

import java.util.List;

public interface PersonalResumeService
{
    /* 简历创建 */
    void create(PersonalResumeReq personalResumeReq) throws XinYuanException;

    /* 简历更改 */
    void updatePerRe(PersonalResumeReq personalResumeReq) throws XinYuanException;

    /* 展示简历 */
    PersonalResume showPersonalResume(Integer userId) throws XinYuanException;

    /* 个人简历列表 */
    List<PersonalResumeVO> showPersonalResumeList(Integer identity, Integer userId) throws XinYuanException;

    /* 筛选简历（登录） */
    List<PersonalResumeVO> selectPersonalResume(String schoolName, String majorName, Integer identity) throws XinYuanException;

    /* 筛选简历（未登录） */
    List<PersonalResumeVO> selectPersonalResumeLogout(String schoolName, String majorName) throws XinYuanException;

    /* 个人简历模糊搜索 */
    List<PersonalResumeVO> personalResumeFuzzySearch(String keyWord, Integer identity, Integer userId) throws XinYuanException;
}
