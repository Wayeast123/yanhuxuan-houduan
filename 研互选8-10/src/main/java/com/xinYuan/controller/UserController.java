package com.xinYuan.controller;

import com.xinYuan.common.ApiRestResponse;
import com.xinYuan.common.Constant;
import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.pojo.User;
import com.xinYuan.model.request.InsertMaterialReq;
import com.xinYuan.model.request.PersonalResumeReq;
import com.xinYuan.model.vo.CollectionVO;
import com.xinYuan.model.vo.InformationReleaseVO;
import com.xinYuan.model.vo.PersonalResumeVO;
import com.xinYuan.model.vo.UserVO;
import com.xinYuan.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 用户控制器
 */
@CrossOrigin
@Controller   ////Controller对外提供接口
public class UserController
{

    @Autowired
    UserService userService;

    @Autowired
    InformationReleaseService informationReleaseService;

    @Autowired
    PersonalResumeService personalResumeService;

    @Autowired
    CollectionService collectionService;

    @Autowired
    VerifyMaterialService verifyMaterialService;
    /**
     * 1.注册
     */
    @PostMapping("/user/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("account") String account,
                                    @RequestParam("password") String password,
                                    @RequestParam("nick") String nick,
                                    @RequestParam("telephone") String telephone)
            throws XinYuanException
    {

        //1.1 做校验
        //1.1.1 昵称不能为空
        if (StringUtils.isEmpty(nick))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_NICK);
        }
        //1.1.2 昵称长度不能超过8位
        else if (nick.length() > 8)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NICK_TOO_LONG);
        }
        //1.1.3 用户名不能为空
        else if (StringUtils.isEmpty(account))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_ACCOUNT);
        }
        //1.1.4 电话不能为空
        else if (StringUtils.isEmpty(telephone))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_TELEPHONE);
        }
        //1.1.5 电话不符合规范
        else if (!Pattern.matches("^1[3-9]\\d{9}$", telephone))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.TELEPHONE_ERROR);
        }
        //1.1.6 密码不能为空
        else if (StringUtils.isEmpty(password))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_PASSWORD);
        }
        //1.1.7 密码长度不能少于8位
        else if (password.length() < 8)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.PASSWORD_TOO_SHORT);
        }



        //1.2 调用注册方法
        userService.register(account, password, nick, telephone);

        return ApiRestResponse.success();
    }

    /**
     * 2.登录
     */
    @PostMapping("/user/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("account") String account,
                                 @RequestParam("password") String password,
                                 HttpServletRequest request) throws XinYuanException
    {
        //2.1 做校验
        //2.1.1 账号不能为空
        if (StringUtils.isEmpty(account))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_ACCOUNT);
        }
        //2.1.2 密码不能为空
        else if (StringUtils.isEmpty(password))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_PASSWORD);
        }

        HttpSession session = request.getSession();

        //2.2 调用登录方法
        User user = userService.login(account, password);

        //2.3 保存用户信息到session，并获取当前会话ID
        user.setPassword(null);
        session.setAttribute(Constant.XIN_YUAN_USER, user);
        String sessionId = session.getId();

        //2.4 封装用户信息以及当前会话ID
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        userVO.setSessionId(sessionId);

        return ApiRestResponse.success(userVO);
    }

    /**
     * 3.更新个性签名（此用户已经处于登录状态）
     */
    @PostMapping("/user/modify")
    @ResponseBody
    public ApiRestResponse modify(HttpServletRequest request,
                                  @RequestParam("sign") String sign,
                                  @RequestParam("nick") String nick
                                 )
            throws XinYuanException
    {
        //3.1 获取当前登录用户（并判断是否处于登录状态）
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //3.2 准备用户信息（）
        User user = new User();
        user.setId(currentUser.getId());
        user.setSign(sign);
        user.setNick(nick);

        //3.3 调用更新方法
        userService.modify(user);

        return ApiRestResponse.success();
    }

    /**
     * 4.登出，清除session
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        session.removeAttribute(Constant.XIN_YUAN_USER);
        return ApiRestResponse.success();
    }

    /**
     * 5.查看导师发布内容
     */
    @GetMapping("/user/InformationReleaseList")
    @ResponseBody
    public ApiRestResponse InformationReleaseList() throws XinYuanException
    {
        //5.1 获取内容列表
        List<InformationReleaseVO> informationReleaseVOList = informationReleaseService.InformationReleaseList();
        return ApiRestResponse.success(informationReleaseVOList);
    }

    /**
     * 6.筛选指定条件的导师发布内容
     */
    @GetMapping("/user/InformationReleaseScreen")
    @ResponseBody
    public ApiRestResponse InformationReleaseScreen(@RequestParam("schoolName") String schoolName,
                                                    @RequestParam("majorName") String majorName)
            throws XinYuanException
    {

        //6.1 获取筛选结果
        List<InformationReleaseVO> informationReleaseVOList = informationReleaseService.InformationReleaseScreen(schoolName, majorName);

        return ApiRestResponse.success(informationReleaseVOList);
    }

    /**
     * 7.创建个人简历
     */
    @PostMapping("/user/createPerRe")
    @ResponseBody
    public ApiRestResponse createPersonalResume(@Valid @RequestBody PersonalResumeReq personalResumeReq,
                                                HttpServletRequest request)
            throws XinYuanException
    {
        //7.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //7.2 做校验
        //7.2.1 姓名不能为空
        if (StringUtils.isEmpty(personalResumeReq.getUserName()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_USERNAME);
        }
        //7.2.2 学校不能为空
        else if (StringUtils.isEmpty(personalResumeReq.getSchoolName()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_SCHOOLNAME);
        }
        //7.2.3 专业不能为空
        else if (StringUtils.isEmpty(personalResumeReq.getMajorName()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_MAJORNAME);
        }
        //7.2.4 电话不能为空
        else if (StringUtils.isEmpty(personalResumeReq.getTelephone()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_TELEPHONE);
        }
        //7.2.5 电话不符合规范
        else if (!Pattern.matches("^1[3-9]\\d{9}$", personalResumeReq.getTelephone()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.TELEPHONE_ERROR);
        }
        //7.2.6 简历内容不能为空
        else if (StringUtils.isEmpty(personalResumeReq.getContent()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.CONTENT_NOT_NULL);
        }

        //7.3 创建简历
        personalResumeReq.setUserId(currentUser.getId());
        personalResumeReq.setIdentity(currentUser.getIdentity());
        personalResumeService.create(personalResumeReq);

        return ApiRestResponse.success();
    }

    /**
     * 8.更新个人简历
     */
    @PostMapping("/user/updatePerRe")
    @ResponseBody
    public ApiRestResponse updatePersonalResume(@Valid @RequestBody PersonalResumeReq personalResumeReq,
                                                HttpServletRequest request)
            throws XinYuanException
    {
        //8.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //8.2 做校验
        //8.2.1 姓名不能为空
        if (StringUtils.isEmpty(personalResumeReq.getUserName()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_USERNAME);
        }
        //8.2.2 学校不能为空
        else if (StringUtils.isEmpty(personalResumeReq.getSchoolName()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_SCHOOLNAME);
        }
        //8.2.3 专业不能为空
        else if (StringUtils.isEmpty(personalResumeReq.getMajorName()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_MAJORNAME);
        }
        //8.2.4 电话不能为空
        else if (StringUtils.isEmpty(personalResumeReq.getTelephone()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_TELEPHONE);
        }
        //8.2.5 电话不符合规范
        else if (!Pattern.matches("^1[3-9]\\d{9}$", personalResumeReq.getTelephone()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.TELEPHONE_ERROR);
        }
        //8.2.6 简历内容不能为空
        else if (StringUtils.isEmpty(personalResumeReq.getContent()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.CONTENT_NOT_NULL);
        }

        //8.3 更新个人简历
        personalResumeReq.setUserId(currentUser.getId());
        personalResumeReq.setIdentity(currentUser.getIdentity());
        personalResumeService.updatePerRe(personalResumeReq);

        return ApiRestResponse.success();
    }

    /**
     * 9.展示个人简历
     */
    @GetMapping("/user/showPerRe")
    @ResponseBody
    public ApiRestResponse showPersonalResume(HttpServletRequest request) throws XinYuanException
    {
        //9.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //9.2 获取个人简历
        PersonalResume personalResume = personalResumeService.showPersonalResume(currentUser.getId());

        return ApiRestResponse.success(personalResume);
    }

    /**
     *  10.个人简历列表
     */
    @GetMapping("/user/showPerReList")
    @ResponseBody
    public ApiRestResponse showPersonalResumeList(HttpServletRequest request) throws XinYuanException
    {
        //10.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null || currentUser.getIdentity() == 1)
        {
            //10.1.1 获取所有个人简历列表
            List<PersonalResumeVO> personalResumeVOList = personalResumeService.showPersonalResumeList(1, 1);
            return ApiRestResponse.success(personalResumeVOList);
        }

        //10.2 导师获取个人简历列表
        if (currentUser.getIdentity() == 2)
        {
            List<PersonalResumeVO> personalResumeVOList = personalResumeService.showPersonalResumeList(2, currentUser.getId());
            return ApiRestResponse.success(personalResumeVOList);
        }

        //10.3 准研究生、在读或已毕业研究生 获取个人简历列表
        List<PersonalResumeVO> personalResumeVOList = personalResumeService.showPersonalResumeList(3, currentUser.getId());
        return ApiRestResponse.success(personalResumeVOList);

    }

    /**
     *  11.筛选个人简历
     */
    @GetMapping("/user/selectPerRe")
    @ResponseBody
    public ApiRestResponse selectPersonalResume(@RequestParam("schoolName") String schoolName,
                                                @RequestParam("majorName") String majorName,
                                                HttpServletRequest request)
            throws XinYuanException
    {
        //11.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        //11.2 未登录应该无区别筛选
        if (currentUser == null)
        {
            List<PersonalResumeVO> personalResumeVOList = personalResumeService.selectPersonalResumeLogout(schoolName, majorName);
            return ApiRestResponse.success(personalResumeVOList);
        }

        //11.3 根据用户身份来决定筛选对象
        //11.3.1 如果用户是普通用户、准研究生、在读或已毕业研究生就只能筛选出导师的个人简历
        if (currentUser.getIdentity().equals(1) || currentUser.getIdentity().equals(3) || currentUser.getIdentity().equals(4))
        {
            List<PersonalResumeVO> personalResumeVOList = personalResumeService.selectPersonalResume(schoolName, majorName, 2);
            return ApiRestResponse.success(personalResumeVOList);
        }
        //11.3.2 如果用户是导师，就只能筛选出准研究生的个人简历
        else if (currentUser.getIdentity().equals(2))
        {
            List<PersonalResumeVO> personalResumeVOList = personalResumeService.selectPersonalResume(schoolName, majorName, 3);
            return ApiRestResponse.success(personalResumeVOList);
        }
        //11.3.3 身份报错
        else
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NO_IDENTITY);
        }
    }

    /**
     * 12. 收藏
     */
    @PostMapping("/user/collect")
    @ResponseBody
    public ApiRestResponse updatePersonalResume(@RequestParam("collectId") Integer collectId,
                                                HttpServletRequest request)
            throws XinYuanException
    {
        //12.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //12.2 收藏
        collectionService.collect(collectId, currentUser.getId());

        return ApiRestResponse.success();
    }

    /**
     *  13.个人收藏列表
     */
    @GetMapping("/user/collectList")
    @ResponseBody
    public ApiRestResponse collectList(HttpServletRequest request) throws XinYuanException
    {
        //13.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //13.2 获取收藏列表
        List<CollectionVO> collectionVOList = collectionService.collectList(currentUser.getId());

        return ApiRestResponse.success(collectionVOList);
    }

    /**
     *  14.取消收藏
     */
    @PostMapping("/user/collectCancel")
    @ResponseBody
    public ApiRestResponse collectCancel(@RequestParam("collectId") Integer collectId, HttpServletRequest request)
            throws XinYuanException
    {
        //12.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //12.2 收藏
        collectionService.collectCancel(collectId);

        return ApiRestResponse.success();
    }

    /**
     * 15. 用户验证身份材料填写
     */
    @PostMapping("/user/verifyIdentity")
    @ResponseBody
    public ApiRestResponse verifyIdentity(@RequestBody InsertMaterialReq insertMaterialReq,
                                          HttpServletRequest request) throws XinYuanException
    {

        //15.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //15.2 做校验
        //15.2.1 姓名不能为空
        if (StringUtils.isEmpty(insertMaterialReq.getUserName()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_USERNAME);
        }
        //15.2.2 年龄不能为空
        else if (StringUtils.isEmpty(insertMaterialReq.getAge()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.AGE_NOT_NULL);
        }
        //15.2.3 籍贯不能为空
        else if (StringUtils.isEmpty(insertMaterialReq.getHometown()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.HOMETOWN_NOT_NULL);
        }
        //15.2.4 身份证号码不能为空
        else if (StringUtils.isEmpty(insertMaterialReq.getIdCardNumber()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.IDCARDNUMBER_NOT_NULL);
        }
        //15.2.4 电话不能为空
        else if (StringUtils.isEmpty(insertMaterialReq.getTelephone()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_TELEPHONE);
        }
        //15.2.5 电话不符合规范
        else if (!Pattern.matches("^1[3-9]\\d{9}$", insertMaterialReq.getTelephone()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.TELEPHONE_ERROR);
        }//15.2.6 身份证正面照不能为空
        else if (StringUtils.isEmpty(insertMaterialReq.getIdCardFront()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.IdCardFront_NOT_NULL);
        }//15.2.7 身份证背面照不能为空
        else if (StringUtils.isEmpty(insertMaterialReq.getIdCardBack()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.IdCardBack_NOT_NULL);
        }//15.2.8 证明身份照不能为空
        else if (StringUtils.isEmpty(insertMaterialReq.getCertificate()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.Certificate_NOT_NULL);
        }

        //15.3 写入数据
        insertMaterialReq.setUserId(currentUser.getId());
        verifyMaterialService.verifyIdentity(insertMaterialReq);

        return ApiRestResponse.success();
    }

    /**
     * 16. 上传身份证正面
     */
    @PostMapping("/user/uploadIdCardFront")
    @ResponseBody
    public ApiRestResponse uploadIdCardFront(HttpServletRequest httpServletRequest,
                                  @RequestParam("file") MultipartFile file)
    {

        //16.1 判断是否登录
        HttpSession session = httpServletRequest.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //16.2 图片处理
        //16.2.1 获取文件的原始名字
        String fileName = file.getOriginalFilename();
        //16.2.2 获取后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //16.2.3 生成文件名称UUID（生成新的文件名）
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        //16.2.4 创建文件夹
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        //16.2.5 判断文件夹是否存在
        if (!fileDirectory.exists())
        {
            if (!fileDirectory.mkdir())
            {
                //如果不存在，再次尝试创建文件夹（如果还是失败，比如权限不够，则抛出异常）
                throw new XinYuanException(XinYuanExceptionEnum.MKDIR_FAILED);
            }
        }
        //16.2.6 把文件从请求中放到指定文件目录下地址中
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //16.2.7 获取文件保存的地址
        String image = null;
        try
        {
            image = getHost(new URI(httpServletRequest.getRequestURL() + ""))
                    + "/images/"
                    + newFileName;
        } catch (URISyntaxException e)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.UPLOAD_FAILED);
        }

        //16.3 调用发布方法
        int userId = currentUser.getId();  //因为查询数据库中对应用户时，需要用到id（只有找到对应用户才能更新数据）
        verifyMaterialService.uploadIdCardFront(userId, image);

        return ApiRestResponse.success(image);
    }

    /**
     * 17. 上传身份证背面
     */
    @PostMapping("/user/uploadIdCardBack")
    @ResponseBody
    public ApiRestResponse uploadIdCardBack(HttpServletRequest httpServletRequest,
                                  @RequestParam("file") MultipartFile file)
    {

        //17.1 判断是否登录
        HttpSession session = httpServletRequest.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //17.2 图片处理
        //17.2.1 获取文件的原始名字
        String fileName = file.getOriginalFilename();
        //17.2.2 获取后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //17.2.3 生成文件名称UUID（生成新的文件名）
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        //17.2.4 创建文件夹
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        //17.2.5 判断文件夹是否存在
        if (!fileDirectory.exists())
        {
            if (!fileDirectory.mkdir())
            {
                //如果不存在，再次尝试创建文件夹（如果还是失败，比如权限不够，则抛出异常）
                throw new XinYuanException(XinYuanExceptionEnum.MKDIR_FAILED);
            }
        }
        //17.2.6 把文件从请求中放到指定文件目录下地址中
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //17.2.7 获取文件保存的地址
        String image = null;
        try
        {
            image = getHost(new URI(httpServletRequest.getRequestURL() + ""))
                    + "/images/"
                    + newFileName;
        } catch (URISyntaxException e)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.UPLOAD_FAILED);
        }

        //17.3 调用发布方法
        int userId = currentUser.getId();  //因为查询数据库中对应用户时，需要用到id（只有找到对应用户才能更新数据）
        verifyMaterialService.uploadIdCardBack(userId, image);

        return ApiRestResponse.success(image);
    }

    /**
     * 18. 上传其它证明
     */
    @PostMapping("/user/uploadCertificate")
    @ResponseBody
    public ApiRestResponse uploadCertificate(HttpServletRequest httpServletRequest,
                                  @RequestParam("file") MultipartFile file)
    {

        //18.1 判断是否登录
        HttpSession session = httpServletRequest.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //18.2 图片处理
        //18.2.1 获取文件的原始名字
        String fileName = file.getOriginalFilename();
        //18.2.2 获取后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //18.2.3 生成文件名称UUID（生成新的文件名）
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        //18.2.4 创建文件夹
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        //18.2.5 判断文件夹是否存在
        if (!fileDirectory.exists())
        {
            if (!fileDirectory.mkdir())
            {
                //如果不存在，再次尝试创建文件夹（如果还是失败，比如权限不够，则抛出异常）
                throw new XinYuanException(XinYuanExceptionEnum.MKDIR_FAILED);
            }
        }
        //18.2.6 把文件从请求中放到指定文件目录下地址中
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //18.2.7 获取文件保存的地址
        String image = null;
        try
        {
            image = getHost(new URI(httpServletRequest.getRequestURL() + ""))
                    + "/images/"
                    + newFileName;
        } catch (URISyntaxException e)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.UPLOAD_FAILED);
        }

        //18.3 调用发布方法
        int userId = currentUser.getId();  //因为查询数据库中对应用户时，需要用到id（只有找到对应用户才能更新数据）
        verifyMaterialService.uploadCertificate(userId, image);

        return ApiRestResponse.success(image);
    }

    // 此方法用于获取当前的IP和端口号
    private URI getHost(URI uri)
    {
        URI effectiveURI;
        try
        {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                    null, null, null);  //新建URI的一种方式（得到经过提取的新的URI）
        } catch (URISyntaxException e)
        {
            effectiveURI = null;   //如果创建失败，则设置为null
        }
        return effectiveURI;
    }

    /**
     * 19. 导师发布信息模糊搜索
     */
    @GetMapping("/user/informationReleaseFuzzySearch")
    @ResponseBody
    public ApiRestResponse informationReleaseFuzzySearch(@RequestParam("keyWord") String keyWord,
                                                         HttpServletRequest request)
            throws XinYuanException
    {
        //19.1 检验
        if (StringUtils.isEmpty(keyWord))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.KeyWord_NOT_NULL);
        }

        //19.2 模糊搜索
        List<InformationReleaseVO> informationReleaseVOList = informationReleaseService.informationReleaseFuzzySearch(keyWord);

        return ApiRestResponse.success(informationReleaseVOList);
    }

    /**
     * 20. 上传头像
     */
    @PostMapping("/user/uploadHeadImage")
    @ResponseBody
    public ApiRestResponse upload(HttpServletRequest httpServletRequest,
                                  @RequestParam("file") MultipartFile file)
    {

        //20.1 判断是否登录以及是否是导师
        HttpSession session = httpServletRequest.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //20.2 图片处理
        //20.2.1 获取文件的原始名字
        String fileName = file.getOriginalFilename();
        //20.2.2 获取后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //20.2.3 生成文件名称UUID（生成新的文件名）
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        //20.2.4 创建文件夹
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        //20.2.5 判断文件夹是否存在
        if (!fileDirectory.exists())
        {
            if (!fileDirectory.mkdir())
            {
                //如果不存在，再次尝试创建文件夹（如果还是失败，比如权限不够，则抛出异常）
                throw new XinYuanException(XinYuanExceptionEnum.MKDIR_FAILED);
            }
        }
        //20.2.6 把文件从请求中放到指定文件目录下地址中
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //20.2.7 获取文件保存的地址
        String image = null;
        try
        {
            image = getHost(new URI(httpServletRequest.getRequestURL() + ""))
                    + "/images/"
                    + newFileName;
        } catch (URISyntaxException e)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.UPLOAD_FAILED);
        }

        //20.3 调用发布方法
        int userId = currentUser.getId();  //因为查询数据库中对应用户时，需要用到id（只有找到对应用户才能更新数据）
        userService.uploadHeadImage(userId, image);

        return ApiRestResponse.success(image);
    }

    /**
     * 19. 个人简历模糊搜索
     */
    @GetMapping("/user/personalResumeFuzzySearch")
    @ResponseBody
    public ApiRestResponse personalResumeFuzzySearch(@RequestParam("keyWord") String keyWord,
                                                     HttpServletRequest request)
            throws XinYuanException
    {
        //19.1 检验
        if (StringUtils.isEmpty(keyWord))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.KeyWord_NOT_NULL);
        }

        //19.2 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null || currentUser.getIdentity() == 1)
        {
            //19.2.1 获取所有导师简历列表
            List<PersonalResumeVO> personalResumeVOList = personalResumeService.personalResumeFuzzySearch(keyWord, 1, 1);
            return ApiRestResponse.success(personalResumeVOList);
        }

        //19.3 导师获取个人简历列表
        if (currentUser.getIdentity() == 2)
        {
            List<PersonalResumeVO> personalResumeVOList = personalResumeService.personalResumeFuzzySearch(keyWord, 2, currentUser.getId());
            return ApiRestResponse.success(personalResumeVOList);
        }

        //19.4 准研究生、在读或已毕业研究生 获取个人简历列表
        List<PersonalResumeVO> personalResumeVOList = personalResumeService.personalResumeFuzzySearch(keyWord, 3, currentUser.getId());
        return ApiRestResponse.success(personalResumeVOList);
    }
}
