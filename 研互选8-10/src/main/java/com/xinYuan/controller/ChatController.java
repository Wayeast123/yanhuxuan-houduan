package com.xinYuan.controller;

import com.xinYuan.common.ApiRestResponse;
import com.xinYuan.common.Constant;
import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.pojo.Chat;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.pojo.User;
import com.xinYuan.service.ChatService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 聊天控制器
 */
@CrossOrigin
@Controller
public class ChatController
{
    @Autowired
    ChatService1 chatService;

    /**
     *  1.获取最新的聊天信息（确保每个发送过的对象都有，而且只筛选出最新那条）
     */
    @GetMapping("/chat/chatList")
    @ResponseBody
    public ApiRestResponse listNewChat(HttpServletRequest request) throws XinYuanException
    {

        //1.1 获取当前登录用户（并判断是否处于登录状态）
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //1.2 获取所需要展示的最新聊天信息
        List<Chat> chatList = chatService.listNewChat(currentUser.getId());

        return ApiRestResponse.success(chatList);
    }

    /**
     *  2.获取与指定对象的聊天记录
     */
    @GetMapping("/chat/getChatPersonal")
    @ResponseBody
    public ApiRestResponse getChatPersonal(Integer chatObject,
                                           HttpServletRequest request) throws XinYuanException
    {

        //2.1 获取当前登录用户（并判断是否处于登录状态）
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //2.2 获取所需要展示的最新聊天信息
        List<Chat> chatList = chatService.getChatPersonal(currentUser.getId(), chatObject);

        return ApiRestResponse.success(chatList);
    }

    /**
     *  3.发送聊天信息（本用户）
     */
    @PostMapping("/chat/sendMessage")
    @ResponseBody
    public ApiRestResponse sendMessage(Integer chatObject,
                                       String sendContent,
                                       HttpServletRequest request) throws XinYuanException
    {
        //3.1 获取当前登录用户（并判断是否处于登录状态）
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //3.2 插入聊天信息
        chatService.sendMessage(currentUser.getId(), chatObject, sendContent);

        //3.3 获取所需要展示的最新聊天信息
        List<Chat> chatList = chatService.getChatPersonal(currentUser.getId(), chatObject);

        return ApiRestResponse.success(chatList);
    }

    /**
     * 4.修改阅读状态为已读（暂时没用上）
     */
    @PostMapping("/chat/updateReadStatus")
    @ResponseBody
    public ApiRestResponse updateReadStatus(Integer chatObject,
                                           HttpServletRequest request) throws XinYuanException
    {
        //4.1 获取当前登录用户（并判断是否处于登录状态）
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //4.2 修改阅读状态为已读
        chatService.updateReadStatus(currentUser.getId(), chatObject);

        return ApiRestResponse.success();
    }

    /**
     * 5.可聊天好友
     */
    @GetMapping("/chat/getChatFriends")
    @ResponseBody
    public ApiRestResponse getChatFriends(HttpServletRequest request) throws XinYuanException
    {
        //5.1 获取当前登录用户（并判断是否处于登录状态）
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //5.2 获取好友的个人简历
        List<PersonalResume> personalResumeList = chatService.getChatFriends(currentUser.getId());

        return ApiRestResponse.success(personalResumeList);
    }

}
