package com.xinYuan.service.impl;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.dao.ChatMapper;
import com.xinYuan.model.dao.PersonalResumeMapper;
import com.xinYuan.model.dao.ResumeSendMapper;
import com.xinYuan.model.pojo.Chat;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.vo.ResumeSendVO;
import com.xinYuan.service.ChatService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService1
{

    @Autowired
    ChatMapper chatMapper;

    @Autowired
    PersonalResumeMapper personalResumeMapper;

    @Autowired
    ResumeSendMapper resumeSendMapper;

    /**
     *  1.获取最新的聊天信息（确保每个发送过的对象都有，而且只筛选出最新那条）(有bug)
     */
    public List<Chat> listNewChat(Integer receiveId) throws XinYuanException
    {
        //1.1 筛选出接收人是本用户的所有信息（按时间最新来筛选排序）
        List<Chat> chatList = chatMapper.listNewChat(receiveId);

        //1.2 将排好序的信息进行筛选，保证发送用户唯一性和最新性
        List<Chat> chatListNew = new ArrayList<>();
        if (chatListNew != null)
        {
            chatListNew.add(chatList.get(0));   //第一个必定属于
        }


        for (int i = 1; i < chatList.size(); i++)
        {
            int flag = 1;

            for (int j = 0; j < chatListNew.size(); j++)
            {
                if (chatList.get(i).getSendId() == chatListNew.get(j).getSendId())
                {
                    flag = 0;
                }
            }

            if (flag == 1)
            {
                chatListNew.add(chatList.get(i));
            }
        }

        return chatListNew;
    }

    /**
     *  2.获取与指定对象的聊天记录
     */
    public List<Chat> getChatPersonal(Integer userId, Integer chatObject) throws XinYuanException
    {
        //2.1 获取对方发过来的消息
        List<Chat> chatListOpposite = chatMapper.selectBySendIdAndReceiveId(chatObject, userId);

        //2.2 获取本用户发过去的消息
        List<Chat> chatListOwn = chatMapper.selectBySendIdAndReceiveId(userId, chatObject);

        //2.3 整合后进行重新排序
        chatListOwn.addAll(chatListOpposite);

        if (chatListOwn != null)
        {
            chatListOwn.sort(Comparator.comparing(Chat :: getCreateTime));
//            Collections.reverse(chatListOwn);
        }

        return chatListOwn;
    }

    /**
     *  3.发送聊天信息（本用户）
     */
    public void sendMessage(Integer sendId, Integer receiveId, String sendContent) throws XinYuanException
    {
        //3.1 准备数据
        Chat chat = new Chat();
        chat.setSendId(sendId);
        chat.setReceiveId(receiveId);
        chat.setSendContent(sendContent);

        PersonalResume personalResume1 = personalResumeMapper.selectByUserId(sendId);
        PersonalResume personalResume2 = personalResumeMapper.selectByUserId(receiveId);

        chat.setSendName(personalResume1.getUserName());
        chat.setReceiveName(personalResume2.getUserName());
        chat.setReadStatus(0);

        //3.2 插入数据
        int count = chatMapper.insertSelective(chat);
        if (count == 0) {
            throw new XinYuanException(XinYuanExceptionEnum.INSERT_FAILED);
        }

    }

    /**
     *  4.修改阅读状态为已读
     */
    public void updateReadStatus(Integer userId, Integer chatObject) throws XinYuanException
    {
        //4.1 获取对方发过来的消息
        List<Chat> chatListOpposite = chatMapper.selectBySendIdAndReceiveId(chatObject, userId);

        //4.2 获取本用户发过去的消息
        List<Chat> chatListOwn = chatMapper.selectBySendIdAndReceiveId(userId, chatObject);

        //4.3 整合后进行重新排序
        chatListOwn.addAll(chatListOpposite);

        if (chatListOwn != null)
        {
            chatListOwn.sort(Comparator.comparing(Chat :: getCreateTime));
        }

        //4.4 逐一修改状态
        for (int i = 0; i < chatListOwn.size(); i++)
        {
            Chat chat = new Chat();
            chat.setId(chatListOwn.get(i).getId());
            chat.setReadStatus(1);
            int updateCount = chatMapper.updateByPrimaryKeySelective(chat);
            if (updateCount > 1) {
                throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
            }
        }
    }

    /**
     *  5.可聊天好友
     */
    public  List<PersonalResume> getChatFriends(Integer userId) throws XinYuanException
    {
        List<PersonalResume> personalResumeList = new ArrayList<>();

        //5.1 获取自己发送的简历被同意的（个人简历）
        List<ResumeSendVO> resumeListByMyself = resumeSendMapper.selectBySendIdAndStatus(userId, 1);
        for (int i = 0; i < resumeListByMyself.size(); i++)
        {
            PersonalResume personalResume = personalResumeMapper.selectByUserId(resumeListByMyself.get(i).getReceiveId());
            personalResumeList.add(personalResume);
        }

        //5.2 获取他人发送的简历自己同意的 （个人简历）
        List<ResumeSendVO> resumeListByHimself = resumeSendMapper.selectByReceiveIdAndStatus(userId, 1);

        //5.3 去掉重复的
        for (int i = 0; i < resumeListByHimself.size(); i++)
        {
            int flag = 1;

            for (int j = 0; j < personalResumeList.size(); j++)
            {
                if (resumeListByHimself.get(i).getSendId() == personalResumeList.get(j).getUserId())
                {
                    flag = 0;
                }
            }

            if (flag == 1)
            {
                PersonalResume personalResume = personalResumeMapper.selectByUserId(resumeListByHimself.get(i).getSendId());
                personalResumeList.add(personalResume);
            }
        }

        return personalResumeList;
    }
}
