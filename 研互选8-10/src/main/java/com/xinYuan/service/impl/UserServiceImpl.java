package com.xinYuan.service.impl;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.dao.InformationReleaseMapper;
import com.xinYuan.model.dao.PersonalResumeMapper;
import com.xinYuan.model.dao.TeacherEvaluationMapper;
import com.xinYuan.model.dao.UserMapper;
import com.xinYuan.model.pojo.InformationRelease;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.pojo.TeacherEvaluation;
import com.xinYuan.model.pojo.User;
import com.xinYuan.model.vo.UserVO;
import com.xinYuan.service.UserService;
import com.xinYuan.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;


@Service   //告诉UserController中的@Autowired ， UserService已经有spring bean类
public class UserServiceImpl implements UserService
{
    @Autowired
    UserMapper userMapper;

    @Autowired
    TeacherEvaluationMapper teacherEvaluationMapper;

    @Autowired
    PersonalResumeMapper personalResumeMapper;

    @Autowired
    InformationReleaseMapper informationReleaseMapper;

    /**
     *  1.注册
     */
    @Override
    public void register(String account, String password, String nick, String telephone) throws XinYuanException
    {

        //1.1 查询用户名是否存在，不允许重名
        User result = userMapper.selectByAccount(account);
        if (result != null)
        {
            throw new XinYuanException(XinYuanExceptionEnum.ACCOUNT_EXISTED);  //进行完这一步后还需对异常进行抛出
        }

        //1.2 查询昵称是否存在，不允许昵称重复
        User result2 = userMapper.selectByNick(nick);
        if (result2 != null)
        {
            throw new XinYuanException(XinYuanExceptionEnum.NICK_EXISTED);  //进行完这一步后还需对异常进行抛出
        }

        //1.3 查询电话号码是否存在，不允许电话号码重复
        User result3 = userMapper.selectByTelephone(telephone);
        if (result3 != null)
        {
            throw new XinYuanException(XinYuanExceptionEnum.TELEPHONE_EXISTED);  //进行完这一步后还需对异常进行抛出
        }

        //1.4 准备用户信息
        User user = new User();
        user.setAccount(account);
        user.setNick(nick);
        user.setTelephone(telephone);
        try
        {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //1.5 把用户信息写进数据库
        int count = userMapper.insertSelective(user);
        if (count == 0)
        {
            throw new XinYuanException(XinYuanExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     *  2.登录（看看此用户是否存在）
     */
    @Override
    public User login(String account, String password) throws XinYuanException
    {

        //2.1 将密码转成MD5形式
        String md5Password = null;
        try
        {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        //2.2 获取登录用户信息
        User user = userMapper.selectLogin(account, md5Password);
        if (user == null)
        {
            throw new XinYuanException(XinYuanExceptionEnum.WRONG_PASSWORD);  //若找不到这个用户，则提示密码错误（防止透露过多信息）
        }
        return user;
    }

    /**
     *  3.更新个性签名
     */
    @Override
    public void modify(User user) throws XinYuanException
    {
        //更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1)
        {
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     *  4.上传头像
     */
    @Override
    public void uploadHeadImage(Integer userId, String image) throws XinYuanException
    {
        User user = new User();
        user.setId(userId);
        user.setHeadImage(image);

        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1)
        {
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     *  5.修改用户身份（管理员在审核通过后使用）
     */
    public void update(User updateUser) throws XinYuanException
    {
        int count = userMapper.updateByPrimaryKeySelective(updateUser);
        if (count == 0) {
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     * 6.用户列表显示
     */
    @Override
    public List<User> userList(Integer identity) throws XinYuanException
    {
        return userMapper.selectIdentity(identity);
    }

    /**
     * 7.用户封禁
     */
    @Override
    public User banOrNoBan(Integer userId, Integer status) throws XinYuanException
    {
        //7.1 修改用户状态 0-正常 1-封禁
        userMapper.updateStatus(userId, status);
        User user = userMapper.selectByPrimaryKey(userId);
        if (user.getStatus() != status)
        {
            throw new XinYuanException(XinYuanExceptionEnum.STATUS_WRONG);
        }
        return user;
    }

    /**
     * 8.获取在读或毕业研究生的导师
     */
    public User getTutor(Integer tutorId) throws XinYuanException
    {
        //8.1 获取在读或毕业研究生的导师
        return userMapper.selectByPrimaryKey(tutorId);
    }

    /**
     * 9.在读或毕业研究生评分功能
     */
    public void assessFraction(Integer userId, Integer tutorId, Integer fraction) throws XinYuanException
    {
        //9.1 查看是否已经评过分
        TeacherEvaluation teacherEvaluationOld = teacherEvaluationMapper.selectByPostgraduateId(userId);
        if (teacherEvaluationOld != null)
        {
            throw new XinYuanException(XinYuanExceptionEnum.ASSESS_REPEAT);
        }

        //9.2 将评分写进数据库
        TeacherEvaluation teacherEvaluation = new TeacherEvaluation();
        teacherEvaluation.setPostgraduateId(userId);
        teacherEvaluation.setTutorId(tutorId);
        teacherEvaluation.setScore(fraction);

        int count = teacherEvaluationMapper.insertSelective(teacherEvaluation);
        if (count == 0)
        {
            throw new XinYuanException(XinYuanExceptionEnum.INSERT_FAILED);
        }

        //9.3 求平均分后更新
        List<TeacherEvaluation> teacherEvaluationList = teacherEvaluationMapper.selectByTutorId(tutorId);
        int sum = 0;
        for (int i = 0; i < teacherEvaluationList.size(); i++)
        {
            sum = sum + teacherEvaluationList.get(i).getScore();
        }
        int average = sum / teacherEvaluationList.size();

        //9.4 更新平均分到各大表
        User user = new User();
        user.setId(tutorId);
        user.setScore(average);
        int updateUser = userMapper.updateByPrimaryKeySelective(user);
        if (updateUser > 1)
        {
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }

        PersonalResume personalResume = new PersonalResume();
        personalResume.setUserId(tutorId);
        personalResume.setScore(average);
        int updatePersonalResume = personalResumeMapper.updateByPrimaryKeySelective(personalResume);
        if (updatePersonalResume > 1)
        {
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }

        InformationRelease informationRelease = new InformationRelease();
        informationRelease.setUserId(tutorId);
        informationRelease.setScore(average);
        int updateInformationRelease = informationReleaseMapper.updateByUserIdSelective(informationRelease);

    }
}
