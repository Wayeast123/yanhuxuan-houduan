package com.xinYuan.model.dao;

import com.xinYuan.model.pojo.InformationRelease;
import com.xinYuan.model.vo.InformationReleaseVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InformationReleaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InformationRelease record);

    int insertSelective(InformationRelease record);

    InformationRelease selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InformationRelease record);

    int updateByUserIdSelective(InformationRelease record);

    int updateByPrimaryKey(InformationRelease record);

    List<InformationRelease> selectByUserId(Integer userId);

    List<InformationReleaseVO> informationReleaseList();

    List<InformationReleaseVO> selectBySchoolName(String schoolName);

    List<InformationReleaseVO> selectByMajorName(String majorName);

    List<InformationReleaseVO> selectBySchoolNameAndMajorName(@Param("schoolName") String schoolName, @Param("majorName") String majorName);

    List<InformationReleaseVO> informationReleaseFuzzySearch(String keyWord);

}