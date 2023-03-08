package com.xinYuan.controller;

import com.xinYuan.common.ApiRestResponse;
import com.xinYuan.common.Constant;
import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import com.xinYuan.model.pojo.User;
import com.xinYuan.model.vo.ResumeSendVO;
import com.xinYuan.service.InformationReleaseService;
import com.xinYuan.service.ResumeSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

/**
 * 导师控制器
 */
@CrossOrigin
@Controller   //Controller对外提供接口
public class TutorController
{
    @Autowired
    InformationReleaseService informationReleaseService;

    @Autowired
    ResumeSendService resumeSendService;

    /**
     *  1.发布信息（此导师已经处于登录状态） 还应该加导师权限的判断（应该优化代码到service层）
     */
    @PostMapping("/tutor/publish")
    @ResponseBody
    public ApiRestResponse publish(@RequestParam("content") String content,
                                   HttpServletRequest request) throws XinYuanException
    {
        //1.1 判断是否登录以及是否是导师
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }else if (currentUser.getIdentity() != 2)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.TUTOR_AUTHORITY);
        }

        //1.2 发布内容不能为空
        if (StringUtils.isEmpty(content))
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.CONTENT_NOT_NULL);
        }

        //1.3 调用发布方法
        int userId = currentUser.getId();  //因为查询数据库中对应用户时，需要用到id（只有找到对应用户才能更新数据）
        informationReleaseService.publish(userId, content);

        return ApiRestResponse.success();
    }

    /**
     *  2.导师上传图片
     */
    @PostMapping("/tutor/uploadFile")
    @ResponseBody
    public ApiRestResponse upload(HttpServletRequest httpServletRequest,
                                  @RequestParam("file") MultipartFile file)
    {

        //2.1 判断是否登录以及是否是导师
        HttpSession session = httpServletRequest.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }else if (currentUser.getIdentity() != 2)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.TUTOR_AUTHORITY);
        }

        //2.2 图片处理
        //2.2.1 获取文件的原始名字
        String fileName = file.getOriginalFilename();
        //2.2.2 获取后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //2.2.3 生成文件名称UUID（生成新的文件名）
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        //2.2.4 创建文件夹
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        //2.2.5 判断文件夹是否存在
        if (!fileDirectory.exists())
        {
            if (!fileDirectory.mkdir())
            {
                //如果不存在，再次尝试创建文件夹（如果还是失败，比如权限不够，则抛出异常）
                throw new XinYuanException(XinYuanExceptionEnum.MKDIR_FAILED);
            }
        }
        //2.2.6 把文件从请求中放到指定文件目录下地址中
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //2.2.7 获取文件保存的地址
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

        //2.3 调用发布方法
        int userId = currentUser.getId();  //因为查询数据库中对应用户时，需要用到id（只有找到对应用户才能更新数据）
        informationReleaseService.uploadFile(userId, image);

        return ApiRestResponse.success(image);
    }

    //2.4 此方法用于获取当前的IP和端口号
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
     * 3.导师意向选择
     */
    @PostMapping("/tutor/selectStudent")
    @ResponseBody
    public ApiRestResponse selectStudent(@RequestParam("resumeId") Integer resumeId,
                                         @RequestParam("selectStatus") Integer selectStatus,
                                         HttpServletRequest request)
    {
        //3.1 获取目前登录用户，如果为空，则是未登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.XIN_YUAN_USER);
        if (currentUser == null)
        {
            return ApiRestResponse.error(XinYuanExceptionEnum.NEED_LOGIN);
        }

        //3.2 修改简历的状态为同意
        resumeSendService.select(resumeId);

        //3.3 导师 根据Status（分为未同意 和 同意）来获取所需的简历
        List<ResumeSendVO> resumeSendVOList = resumeSendService.showInvitation(currentUser.getId(), selectStatus);
        return ApiRestResponse.success(resumeSendVOList);
    }

}
