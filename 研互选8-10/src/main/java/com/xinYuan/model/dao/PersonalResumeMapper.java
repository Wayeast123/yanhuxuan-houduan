package com.xinYuan.model.dao;

import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.vo.InformationReleaseVO;
import com.xinYuan.model.vo.PersonalResumeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalResumeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonalResume record);

    int insertSelective(PersonalResume record);

    PersonalResume selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonalResume record);

    int updateByPrimaryKey(PersonalResume record);

    PersonalResume selectByUserId(Integer userId);

    List<PersonalResumeVO> selectByAll();

    //导师 看 准研究生
    List<PersonalResumeVO> selectStudent(@Param("identity")Integer identity, @Param("targetSchool")String targetSchool);

    //准研究生 看 导师
    List<PersonalResumeVO> selectTutor(@Param("identity")Integer identity, @Param("schoolName")String schoolName);

    List<PersonalResumeVO> selectBySchoolName(@Param("schoolName")String schoolName, @Param("identity")Integer identity);

    List<PersonalResumeVO> selectByMajorName(@Param("majorName")String majorName, @Param("identity")Integer identity);

    List<PersonalResumeVO> selectBySchoolNameAndMajorName(@Param("schoolName")String schoolName, @Param("majorName")String majorName, @Param("identity")Integer identity);

    List<PersonalResumeVO> selectBySchoolNameLogout(String schoolName);

    List<PersonalResumeVO> selectByMajorNameLogout(String majorName);

    List<PersonalResumeVO> selectBySchoolNameAndMajorNameLogout(@Param("schoolName") String schoolName, @Param("majorName") String majorName);

    //导师的筛选学生简历有不同
    List<PersonalResumeVO> selectByTargetSchool(@Param("targetSchool")String targetSchool, @Param("identity")Integer identity);

    List<PersonalResumeVO> selectByTargetMajor(@Param("targetMajor")String targetMajor, @Param("identity")Integer identity);

    List<PersonalResumeVO> selectByTargetSchoolAndTargetMajor(@Param("targetSchool")String targetSchool, @Param("targetMajor")String targetMajor, @Param("identity")Integer identity);

    List<PersonalResumeVO> selectByAllTutor(@Param("identity")Integer identity);

    List<PersonalResumeVO> FuzzySearchByAllTutor(@Param("keyWord")String keyWord, @Param("identity")Integer identity);

    List<PersonalResumeVO> FuzzySearchStudent(@Param("keyWord")String keyWord, @Param("identity")Integer identity, @Param("targetSchool")String targetSchool);

    List<PersonalResumeVO> FuzzySearchTutor(@Param("keyWord")String keyWord, @Param("identity")Integer identity, @Param("schoolName")String schoolName);
}