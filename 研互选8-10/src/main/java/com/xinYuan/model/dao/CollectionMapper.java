package com.xinYuan.model.dao;

import com.xinYuan.model.pojo.Collection;
import com.xinYuan.model.vo.CollectionVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Collection record);

    int insertSelective(Collection record);

    Collection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);

    List<CollectionVO> selectByAll(Integer collectorId);
}