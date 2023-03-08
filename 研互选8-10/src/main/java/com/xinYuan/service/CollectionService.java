package com.xinYuan.service;

import com.xinYuan.exception.XinYuanException;
import com.xinYuan.model.vo.CollectionVO;

import java.util.List;

public interface CollectionService
{
    /* 简历收藏 */
    void collect(Integer collectId, Integer userId) throws XinYuanException;

    /* 收藏列表 */
    List<CollectionVO> collectList(Integer userId) throws XinYuanException;

    /* 取消收藏 */
    void collectCancel(Integer collectId) throws XinYuanException;
}
