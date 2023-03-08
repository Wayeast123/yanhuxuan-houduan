package com.xinYuan.model.dao;

import com.xinYuan.model.pojo.ResumeSend;
import com.xinYuan.model.vo.ResumeSendVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeSendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ResumeSend record);

    int insertSelective(ResumeSend record);

    ResumeSend selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ResumeSend record);

    int updateByPrimaryKey(ResumeSend record);

    ResumeSend selectByUserIdAndObjectId(@Param("sendId")Integer sendId, @Param("receiveId")Integer receiveId);

    List<ResumeSendVO> selectByReceiveIdAndStatus(@Param("receiveId") Integer receiveId, @Param("status") Integer status);

    List<ResumeSendVO> selectBySendIdAndStatus(@Param("sendId") Integer sendId, @Param("status") Integer status);
}