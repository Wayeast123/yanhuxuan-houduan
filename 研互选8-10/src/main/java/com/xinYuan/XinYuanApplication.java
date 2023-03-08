package com.xinYuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//让MyBatis能找到dao下的文件
@EnableSwagger2   //代表开启Swagger能力，自动生成API文档
@EnableCaching  //使用spring提供的cache功能（打开缓存功能）
@MapperScan(basePackages = "com.xinYuan.model.dao")
public class XinYuanApplication {

    public static void main(String[] args) {
        SpringApplication.run(XinYuanApplication.class, args);
    }

}
