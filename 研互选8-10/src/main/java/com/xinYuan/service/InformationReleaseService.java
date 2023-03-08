package com.xinYuan.service;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.model.pojo.InformationRelease;
import com.xinYuan.model.vo.InformationReleaseVO;

import java.util.List;

public interface InformationReleaseService
{
    //用户获取导师发布的信息
    List<InformationReleaseVO> InformationReleaseList() throws XinYuanException;

    //筛选指定条件的发布内容
    List<InformationReleaseVO> InformationReleaseScreen(String schoolName, String majorName) throws XinYuanException;

    //导师发布消息
    void publish(int userId, String content) throws XinYuanException;

    //导师上传发布信息图片
    void uploadFile(int userId, String image) throws XinYuanException;

    //导师发布信息模糊搜索
    List<InformationReleaseVO> informationReleaseFuzzySearch(String keyWord) throws XinYuanException;
}