package com.xinYuan.model.dao;

import com.xinYuan.model.pojo.Chat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Chat record);

    int insertSelective(Chat record);

    Chat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Chat record);

    int updateByPrimaryKey(Chat record);

    List<Chat> listNewChat(Integer receiveId);

    List<Chat> selectBySendIdAndReceiveId(@Param("sendId") Integer sendId, @Param("receiveId") Integer receiveId);
}