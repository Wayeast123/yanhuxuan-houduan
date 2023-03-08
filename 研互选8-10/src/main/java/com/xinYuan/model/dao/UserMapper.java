package com.xinYuan.model.dao;

import com.xinYuan.model.pojo.User;
import com.xinYuan.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //根据账号获取用户
    User selectByAccount(String account);

    //根据昵称获取用户
    User selectByNick(String nick);

    //根据电活号码获取用户
    User selectByTelephone(String telephone);

    //登录
    User selectLogin(@Param("account") String account, @Param("password") String password);

    /* 通过身份查找 */
    List<User> selectIdentity(@Param("identity") Integer identity);

    /* 更改状态 */
    int updateStatus(Integer id, Integer status);
}