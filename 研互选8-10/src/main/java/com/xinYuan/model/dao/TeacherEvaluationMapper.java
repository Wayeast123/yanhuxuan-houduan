package com.xinYuan.model.dao;

import com.xinYuan.model.pojo.TeacherEvaluation;
import com.xinYuan.model.pojo.VerifyMaterial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherEvaluationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TeacherEvaluation record);

    int insertSelective(TeacherEvaluation record);

    TeacherEvaluation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TeacherEvaluation record);

    int updateByPrimaryKey(TeacherEvaluation record);

    TeacherEvaluation selectByPostgraduateId(Integer postgraduateId);

    List<TeacherEvaluation> selectByTutorId(Integer tutorId);
}