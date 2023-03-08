package com.xinYuan.config;

import com.xinYuan.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 描述：     配置地址映射
 */
@Configuration
public class XinYuanWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //把后台管理模块路由到classpath:/static/admin前端文件下
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin");

        //把这个 /images/** 字段后的文件名与 Constant.FILE_UPLOAD_DIR 中的 file: 映射对应起来
        registry.addResourceHandler("/images/**").addResourceLocations(
                "file:" + Constant.FILE_UPLOAD_DIR);

        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");  //把swagger-ui.html地址对应到classpath:/META-INF/resources/目录下
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }
}
