package com.xinYuan.service;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.model.pojo.User;
import com.xinYuan.model.vo.UserVO;

import java.util.List;

public interface UserService
{
    //注册
    void register(String account, String password, String nick, String telephone) throws XinYuanException;

    //登录
    User login(String account, String password) throws XinYuanException;

    //更新个性签名
    void modify(User user) throws XinYuanException;

    //上传头像
    void uploadHeadImage(Integer userId, String image);

    //审核通过接口（管理员）
    void update(User updateUser) throws XinYuanException;

    /* 用户列表显示 */
    List<User> userList(Integer identity) throws XinYuanException;

    /* 用户封禁与解封 */
    User banOrNoBan(Integer userId, Integer status) throws XinYuanException;

    /* 获取在读或毕业研究生的导师 */
    User getTutor(Integer tutorId) throws XinYuanException;

    /* 在读或毕业研究生评分功能 */
    void assessFraction(Integer userId, Integer tutorId, Integer fraction) throws XinYuanException;
}
