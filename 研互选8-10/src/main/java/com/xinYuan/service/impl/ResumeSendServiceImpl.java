package com.xinYuan.service.impl;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.dao.PersonalResumeMapper;
import com.xinYuan.model.dao.ResumeSendMapper;
import com.xinYuan.model.dao.UserMapper;
import com.xinYuan.model.pojo.PersonalResume;
import com.xinYuan.model.pojo.ResumeSend;
import com.xinYuan.model.pojo.User;
import com.xinYuan.model.request.SentResumeReq;
import com.xinYuan.model.vo.ResumeSendVO;
import com.xinYuan.service.ResumeSendService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeSendServiceImpl implements ResumeSendService
{
    @Autowired
    ResumeSendMapper resumeSendMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PersonalResumeMapper personalResumeMapper;

    /**
     * 1.简历发送
     */
    @Override
    public void sentResume(SentResumeReq sentResumeReq) throws XinYuanException
    {
        //1.1 判断该用户是否是导师或者准研究生
        User SendUser = userMapper.selectByPrimaryKey(sentResumeReq.getSendId());
        if (SendUser.getIdentity() != 2 && SendUser.getIdentity() != 3)
        {
            throw new XinYuanException(XinYuanExceptionEnum.NO_SENT_AUTHORITY);
        }

        if (SendUser.getIdentity() == 2)
        {
            PersonalResume receivePersonalResume = personalResumeMapper.selectByUserId(sentResumeReq.getReceiveId());
            if (!sentResumeReq.getSendSchool().equals(receivePersonalResume.getTargetSchool()))
            {
                throw new XinYuanException(XinYuanExceptionEnum.SCHOOL_DIFFERENT);
            }
        }

        if (SendUser.getIdentity() == 3)
        {
            PersonalResume receivePersonalResume = personalResumeMapper.selectByUserId(sentResumeReq.getReceiveId());
            PersonalResume sendPersonalResume = personalResumeMapper.selectByUserId(sentResumeReq.getSendId());
            if (!sendPersonalResume.getTargetSchool().equals(receivePersonalResume.getSchoolName()))
            {
                throw new XinYuanException(XinYuanExceptionEnum.SCHOOL_DIFFERENT);
            }
        }


        //1.2 判断是否已发送了简历
        ResumeSend resumeOld = resumeSendMapper.selectByUserIdAndObjectId(sentResumeReq.getSendId(), sentResumeReq.getReceiveId());
        if (resumeOld != null)
        {
            throw new XinYuanException(XinYuanExceptionEnum.NO_SENT_REPEAT);
        }

        //1.3 将所发送的简历写进数据库
        ResumeSend resumeSend = new ResumeSend();
        BeanUtils.copyProperties(sentResumeReq, resumeSend);
        int count = resumeSendMapper.insertSelective(resumeSend);
        if (count == 0)
        {
            throw new XinYuanException(XinYuanExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 2.准研究生和导师 删除投递简历（接口名最后改）
     */
    public void deleteResume(Integer resumeId) throws XinYuanException
    {
        //2.1 撤销简历
        int count = resumeSendMapper.deleteByPrimaryKey(resumeId);
        if (count == 0)
        {
            throw new XinYuanException(XinYuanExceptionEnum.DELETE_FAILED);
        }
    }

    /**
     * 3.展示受邀请的简历（分为未同意 和 同意）
     */
    public List<ResumeSendVO> showInvitation(Integer receiveId, Integer selectStatus) throws XinYuanException
    {
        //3.1 获取对应要求的简历
        return resumeSendMapper.selectByReceiveIdAndStatus(receiveId, selectStatus);
    }

    /**
     * 4.展示自己投递的简历（分为未同意 和 同意）
     */
    public List<ResumeSendVO> showSent(Integer sendId, Integer selectStatus) throws XinYuanException
    {
        //4.1 获取对应要求的简历
        return resumeSendMapper.selectBySendIdAndStatus(sendId, selectStatus);
    }

    /**
     *  5.(导师)意向选择
     */
    @Override
    public void select(Integer resumeId) throws XinYuanException
    {
        //2.1 准备所需改状态的简历数据
        ResumeSend resumeSend = new ResumeSend();
        resumeSend.setId(resumeId);
        resumeSend.setStatus(1);

        //2.2 修改简历的状态为同意（更新可能情况为0或1）
        int updateCount = resumeSendMapper.updateByPrimaryKeySelective(resumeSend);
        if (updateCount > 1)
        {
            throw new XinYuanException(XinYuanExceptionEnum.UPDATE_FAILED);
        }
    }
}
