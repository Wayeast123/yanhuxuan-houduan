package com.xinYuan.controller;

import com.xinYuan.common.ApiRestResponse;
import com.xinYuan.common.Constant;
import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.pojo.User;
import com.xinYuan.model.vo.InformationReleaseVO;
import com.xinYuan.model.vo.PersonalResumeVO;
import com.xinYuan.model.vo.ResumeSendVO;
import com.xinYuan.service.PersonalResumeService;
import com.xinYuan.service.ResumeSendService;
import com.xinYuan.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 准研究生控制器
 */
@CrossOrigin
@Controller   //Controller对外提供接口
public class StudentController
{
    @Autowired
    ResumeSendService resumeSendService;

    @Autowired
    UserService userService;

    @Autowired
    PersonalResumeService personalResumeService;

    /**
     *  1.准研究生 意向选择
     */
    @PostMapping("/student/selectTutor")
    @ResponseBody
    public ApiRestResponse selectTutor(@RequestParam("resumeId") Integer resumeId,
                                          @RequestParam("selectStatus") Integer selectStatus,
                                          HttpServletRequest request)
    {
        //1.1 获取目前登录用户，如果为空，则是未登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //1.2 修改简历的状态为同意
        resumeSendService.select(resumeId);

        //1.3 准研究生 根据selectStatus（分为未同意 和 同意）来获取所需的简历
        List<ResumeSendVO> resumeSendVOList = resumeSendService.showInvitation(currentUser.getId(), selectStatus);
        return ApiRestResponse.success(resumeSendVOList);
    }

    /**
     * 2.在读或毕业研究生评分功能
     */
    @PostMapping("/postgraduate/assessFraction")
    @ResponseBody
    public ApiRestResponse assessFraction(@RequestParam("fraction") Integer fraction,
                                          HttpServletRequest request)
            throws XinYuanException
    {
        //14.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        userService.assessFraction(currentUser.getId(), currentUser.getTutorId(), fraction);
        return ApiRestResponse.success();
    }

    /**
     * 3.获取在读或毕业研究生的导师
     */
    @GetMapping("/postgraduate/getTutor")
    @ResponseBody
    public ApiRestResponse getTutor(HttpServletRequest request)
            throws XinYuanException
    {
        //3.1 获取目前登录用户，如果为空，则是未登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        PersonalResumeVO personalResumeVO = new PersonalResumeVO();

        User user = userService.getTutor(currentUser.getTutorId());

        PersonalResume personalResume = personalResumeService.showPersonalResume(currentUser.getTutorId());

        BeanUtils.copyProperties(personalResume, personalResumeVO);

        personalResumeVO.setHeadImage(user.getHeadImage());

        return ApiRestResponse.success(personalResumeVO);
    }
}
