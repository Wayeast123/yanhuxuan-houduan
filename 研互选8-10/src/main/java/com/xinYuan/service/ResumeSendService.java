package com.xinYuan.service;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.model.request.SentResumeReq;
import com.xinYuan.model.vo.ResumeSendVO;

import java.util.List;

public interface ResumeSendService
{
    /* 简历发送 */
    void sentResume(SentResumeReq sentResumeReq) throws XinYuanException;

    /* 删除投递简历 */
    void deleteResume(Integer resumeId) throws XinYuanException;

    /* 展示受邀请的简历 */
    List<ResumeSendVO> showInvitation(Integer receiveId, Integer selectStatus) throws XinYuanException;

    /* 展示自己投递的简历 */
    List<ResumeSendVO> showSent(Integer sendId, Integer selectStatus) throws XinYuanException;

    /* 意向选择(导师选学生) */
    void select(Integer resumeId) throws XinYuanException;
}
