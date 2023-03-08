package com.xinYuan.service;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.model.pojo.Chat;
import com.xinYuan.model.pojo.PersonalResume;

import java.util.List;

public interface ChatService1
{
    List<Chat>  listNewChat(Integer receiveId) throws XinYuanException;

    List<Chat> getChatPersonal(Integer userId, Integer chatObject) throws XinYuanException;

    void sendMessage(Integer sendId, Integer receiveId, String sendContent) throws XinYuanException;

    void updateReadStatus(Integer userId, Integer chatObject) throws XinYuanException;

    List<PersonalResume> getChatFriends(Integer userId) throws XinYuanException;
}
