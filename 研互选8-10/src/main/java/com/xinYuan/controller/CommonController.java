package com.xinYuan.controller;


import com.xinYuan.common.ApiRestResponse;
import com.xinYuan.common.Constant;
import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.dao.PersonalResumeMapper;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.pojo.User;
import com.xinYuan.model.request.SentResumeReq;
import com.xinYuan.model.vo.ResumeSendVO;
import com.xinYuan.service.ResumeSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 多角色共有控制器
 */
@CrossOrigin
@Controller   ////Controller对外提供接口
public class CommonController
{

    @Autowired
    ResumeSendService resumeSendService;

    @Autowired
    PersonalResumeMapper personalResumeMapper;

    /**
     *  1. 准研究生和导师 投递简历
     */
    @PostMapping("/studentAndTeacher/sentResume")
    @ResponseBody
    public ApiRestResponse sentResume(@RequestBody SentResumeReq sentResumeReq, HttpServletRequest request)
            throws XinYuanException
    {
        //1.1 获取目前登录用户，如果为空，则是未登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //1.2 投递内容验证
        //1.2.1 姓名不能为空
        if (StringUtils.isEmpty(sentResumeReq.getSendName()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_USERNAME);
        }
        //1.2.2 学校不能为空
        else if (StringUtils.isEmpty(sentResumeReq.getSendSchool()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_SCHOOLNAME);
        }
        //1.2.3 专业不能为空
        else if (StringUtils.isEmpty(sentResumeReq.getSendMajor()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_MAJORNAME);
        }
        //1.2.4 电话不能为空
        else if (StringUtils.isEmpty(sentResumeReq.getSendTelephone()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_TELEPHONE);
        }
        //1.2.5 电话不符合规范
        else if (!Pattern.matches("^1[3-9]\\d{9}$", sentResumeReq.getSendTelephone()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.TELEPHONE_ERROR);
        }
        //1.2.6 简历内容不能为空
        else if (StringUtils.isEmpty(sentResumeReq.getSendContent()))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.CONTENT_NOT_NULL);
        }

        //1.3 投递简历
        sentResumeReq.setSendId(currentUser.getId());
        resumeSendService.sentResume(sentResumeReq);

        return ApiRestResponse.success();
    }

    /**
     * 2.准研究生和导师 删除投递简历（接口名最后改）
     */
    @PostMapping("/studentAndTeacher/deleteResume")
    @ResponseBody
    public ApiRestResponse deleteResume(@RequestParam("resumeId") Integer resumeId,
                                        @RequestParam("selectStatus") Integer selectStatus,
                                        HttpServletRequest request)
            throws XinYuanException
    {
        //2.1 获取目前登录用户，如果为空，则是未登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //2.2 删除投递简历
        resumeSendService.deleteResume(resumeId);

        //2.3 导师 根据Status（分为未同意 和 同意）来获取所需的简历
        if (currentUser.getIdentity().equals(2))
        {
            List<ResumeSendVO> resumeSendVOList = resumeSendService.showInvitation(currentUser.getId(), selectStatus);
            return ApiRestResponse.success(resumeSendVOList);
        }

        //2.4 准研究生 根据Status（分为未同意 和 同意）来获取所需的简历
        List<ResumeSendVO> resumeSendVOList = resumeSendService.showSent(currentUser.getId(), selectStatus);
        return ApiRestResponse.success(resumeSendVOList);
    }

    /**
     *  3. 准研究生和导师 删除邀请函（接口名最后改）
     */
    @PostMapping("/studentAndTeacher/deleteInvitation")
    @ResponseBody
    public ApiRestResponse deleteInvitation(@RequestParam("resumeId") Integer resumeId,
                                         @RequestParam("selectStatus") Integer selectStatus,
                                         HttpServletRequest request)
            throws XinYuanException
    {
        //2.1 获取目前登录用户，如果为空，则是未登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //2.2 撤销简历
        resumeSendService.deleteResume(resumeId);

        //13.2 导师 根据Status（分为未同意 和 同意）来获取所需的简历
        if (currentUser.getIdentity().equals(2))
        {
            List<ResumeSendVO> resumeSendVOList = resumeSendService.showSent(currentUser.getId(), selectStatus);
            return ApiRestResponse.success(resumeSendVOList);
        }

        //13.3 准研究生 根据Status（分为未同意 和 同意）来获取所需的简历
        List<ResumeSendVO> resumeSendVOList = resumeSendService.showInvitation(currentUser.getId(), selectStatus);
        return ApiRestResponse.success(resumeSendVOList);
    }

    /**
     * 4.学生简历/投递简历（分为未同意 和 同意）
     */
    @GetMapping("/studentAndTeacher/showSent")
    @ResponseBody
    public ApiRestResponse showSent(@RequestParam("selectStatus") Integer selectStatus,
                                    HttpServletRequest request)
            throws XinYuanException
    {
        //4.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //4.2 导师 根据Status（分为未同意 和 同意）来获取所需的简历
        if (currentUser.getIdentity().equals(2))
        {
            List<ResumeSendVO> resumeSendVOList = resumeSendService.showInvitation(currentUser.getId(), selectStatus);
            return ApiRestResponse.success(resumeSendVOList);
        }

        //4.3 准研究生 根据Status（分为未同意 和 同意）来获取所需的简历 （修改）
        List<ResumeSendVO> oldResumeSendVOList = resumeSendService.showSent(currentUser.getId(), selectStatus);

        List<ResumeSendVO> newResumeSendVOList = new ArrayList<>();
        for (int i = 0; i < oldResumeSendVOList.size(); i++)
        {
            PersonalResume personalResume = personalResumeMapper.selectByUserId(oldResumeSendVOList.get(i).getReceiveId());
            ResumeSendVO resumeSendVO = new ResumeSendVO();
            resumeSendVO.setId(oldResumeSendVOList.get(i).getId());
            resumeSendVO.setSendName(personalResume.getUserName());
            resumeSendVO.setSendSchool(personalResume.getSchoolName());
            resumeSendVO.setSendMajor(personalResume.getMajorName());
            resumeSendVO.setSendTelephone(personalResume.getTelephone());
            resumeSendVO.setSendContent(personalResume.getContent());
            resumeSendVO.setCreateTime(oldResumeSendVOList.get(i).getCreateTime());
            newResumeSendVOList.add(resumeSendVO);
        }

        return ApiRestResponse.success(newResumeSendVOList);
    }

    /**
     * 5.邀请函（分为未同意 和 同意）
     */
    @GetMapping("/studentAndTeacher/showInvitation")
    @ResponseBody
    public ApiRestResponse showInvitation(@RequestParam("selectStatus") Integer selectStatus,
                                          HttpServletRequest request)
            throws XinYuanException
    {
        //5.1 判断用户是否登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //5.2 导师 根据Status（分为未同意 和 同意）来获取所需的简历
        if (currentUser.getIdentity().equals(2))
        {
            List<ResumeSendVO> oldResumeSendVOList = resumeSendService.showSent(currentUser.getId(), selectStatus);

            List<ResumeSendVO> newResumeSendVOList = new ArrayList<>();
            for (int i = 0; i < oldResumeSendVOList.size(); i++)
            {
                PersonalResume personalResume = personalResumeMapper.selectByUserId(oldResumeSendVOList.get(i).getReceiveId());
                ResumeSendVO resumeSendVO = new ResumeSendVO();
                resumeSendVO.setId(oldResumeSendVOList.get(i).getId());
                resumeSendVO.setSendName(personalResume.getUserName());
                resumeSendVO.setSendSchool(personalResume.getSchoolName());
                resumeSendVO.setSendMajor(personalResume.getMajorName());
                resumeSendVO.setSendTelephone(personalResume.getTelephone());
                resumeSendVO.setSendContent(personalResume.getContent());
                resumeSendVO.setCreateTime(oldResumeSendVOList.get(i).getCreateTime());
                newResumeSendVOList.add(resumeSendVO);
            }

            return ApiRestResponse.success(newResumeSendVOList);
        }

        //5.3 准研究生 根据Status（分为未同意 和 同意）来获取所需的简历
        List<ResumeSendVO> resumeSendVOList = resumeSendService.showInvitation(currentUser.getId(), selectStatus);
        return ApiRestResponse.success(resumeSendVOList);
    }
}
