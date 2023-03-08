package com.xinYuan.controller;

import com.xinYuan.common.ApiRestResponse;
import com.xinYuan.common.Constant;
import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.dao.TeacherEvaluationMapper;
import com.xinYuan.model.dao.VerifyMaterialMapper;
import com.xinYuan.model.pojo.User;
import com.xinYuan.model.pojo.VerifyMaterial;
import com.xinYuan.model.vo.AdminVO;
import com.xinYuan.model.vo.UserVO;
import com.xinYuan.service.UserService;
import com.xinYuan.service.VerifyMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 管理员控制器
 */
@CrossOrigin
@Controller
public class AdminController
{

    @Autowired
    UserService userService;

    @Autowired
    VerifyMaterialService verifyMaterialService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    VerifyMaterialMapper verifyMaterialMapper;

    /**
     * 1.管理员登录接口
     */
    @PostMapping("/admin/login")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("account") String account,
                                      @RequestParam("password") String password)
            throws XinYuanException
    {
        //1.1 校验输入信息是否正确
        if (StringUtils.isEmpty(account))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_ACCOUNT);
        }
        if (StringUtils.isEmpty(password))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_PASSWORD);
        }

        //1.2 获取管理员信息
        User admin = userService.login(account, password);

        //1.3 校验是否是管理员
        if (admin.getIdentity().equals(0))
        {
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            stringRedisTemplate.opsForValue().set(token, String.valueOf(admin.getId()), 3600, TimeUnit.SECONDS);

            //2.4 封装用户信息以及当前会话ID
            AdminVO adminVO = new AdminVO();
            BeanUtils.copyProperties(admin, adminVO);
            adminVO.setToken(token);

            return ApiRestResponse.success(adminVO);

        } else
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_ADMIN);
        }
    }

    /**
     *  2.获取审核信息（管理员）
     */
    @GetMapping("/admin/auditList")
    @ResponseBody
    public ApiRestResponse adminAudit(@RequestParam("identity") Integer identity, HttpServletRequest request)
            throws XinYuanException
    {
        //2.1 获取当前登录用户（并判断是否处于登录状态）
        String token = request.getHeader("token");
        if(token != null)
        {
            String id = stringRedisTemplate.opsForValue().get(token);
            if(id == null)
            {
                throw new XinYuanException(XinYuanExceptionEnum.NO_USERINFO);
            }
        } else
        {
            throw new XinYuanException(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //2.2 根据不同审核区获取不同用户的审核信息
        List<VerifyMaterial> verifyMaterialList = verifyMaterialService.auditInfo(identity);
        return ApiRestResponse.success(verifyMaterialList);
    }


    /**
     *  3.审核通过接口（管理员）
     */
    @PostMapping("/admin/approve")
    @ResponseBody
    public ApiRestResponse approve(@RequestParam("userId") Integer userId,
                                   @RequestParam("identity") Integer identity,
                                   HttpServletRequest request) throws XinYuanException
    {
        //3.1 获取当前登录用户（并判断是否处于登录状态）
        String token = request.getHeader("token");
        if(token != null)
        {
            String id = stringRedisTemplate.opsForValue().get(token);
            if(id == null)
            {
                throw new XinYuanException(XinYuanExceptionEnum.NO_USERINFO);
            }
        } else
        {
            throw new XinYuanException(XinYuanExceptionEnum.NEED_LOGIN);
        }


        User updateUser = new User();

        //3.2 通过导师审核
        if(identity.equals(2))
        {
            updateUser.setId(userId);
            updateUser.setIdentity(2);
            userService.update(updateUser);
            int count = verifyMaterialMapper.deleteByUserId(userId);
            if (count == 0)
            {
                throw new XinYuanException(XinYuanExceptionEnum.DELETE_FAILED);
            }


            return ApiRestResponse.success();
        }
        //3.3 通过准研究生审核
        else if(identity.equals(3))
        {
            updateUser.setId(userId);
            updateUser.setIdentity(3);
            userService.update(updateUser);
            int count = verifyMaterialMapper.deleteByUserId(userId);
            if (count == 0)
            {
                throw new XinYuanException(XinYuanExceptionEnum.DELETE_FAILED);
            }


            return ApiRestResponse.success();
        }
        //3.4 通过在读/已毕业研究生审核
        else
        {
            updateUser.setId(userId);
            updateUser.setIdentity(4);
            userService.update(updateUser);
            int count = verifyMaterialMapper.deleteByUserId(userId);
            if (count == 0)
            {
                throw new XinYuanException(XinYuanExceptionEnum.DELETE_FAILED);
            }


            return ApiRestResponse.success();
        }
    }

    /**
     * 4.登出，清除session
     */
    @PostMapping("/admin/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpServletRequest request)
    {
        stringRedisTemplate.opsForValue().getOperations().delete("token");
        return ApiRestResponse.success();
    }

    /**
     *  5.用户分类显示
     */
    @PostMapping("/admin/userList")
    @ResponseBody
    public ApiRestResponse list(@RequestParam("identity") Integer identity, HttpServletRequest request) throws XinYuanException
    {
        //5.1 获取当前登录用户（并判断是否处于登录状态）
        String token = request.getHeader("token");
        if(token != null)
        {
            String id = stringRedisTemplate.opsForValue().get(token);
            if(id == null)
            {
                throw new XinYuanException(XinYuanExceptionEnum.NO_USERINFO);
            }
        } else
        {
            throw new XinYuanException(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //5.2 获取不同分类的列表 普通用户 导师 准研究生 在读或毕业研究生
        List<User> userList = userService.userList(identity);

        return ApiRestResponse.success(userList);
    }

    /**
     * 6.用户封禁 (管理员)
     */
    @PostMapping("/admin/banUser")
    @ResponseBody
    public ApiRestResponse banUser(@RequestParam("userId") Integer userId,@RequestParam("status") Integer status, HttpServletRequest request)
    {
        //5.1 获取当前登录用户（并判断是否处于登录状态）
        String token = request.getHeader("token");
        if(token != null)
        {
            String id = stringRedisTemplate.opsForValue().get(token);
            if(id == null)
            {
                throw new XinYuanException(XinYuanExceptionEnum.NO_USERINFO);
            }
        } else
        {
            throw new XinYuanException(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //5.2 用户封禁 0为正常状态 1为封禁状态
        User user = userService.banOrNoBan(userId, status);
        if (user.getStatus() == status)
        {
            if (user.getStatus() == 1)
            {
                return ApiRestResponse.success("用户封禁成功");
            }else
            {
                return ApiRestResponse.success("用户解封成功");
            }
        }else
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.STATUS_WRONG);
        }
    }

}
