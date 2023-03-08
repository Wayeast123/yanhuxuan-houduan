package com.xinYuan.common;

import com.google.common.collect.Sets;
import com.xinYuan.exception.XinYuanException;
import com.xinYuan.exception.XinYuanExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 描述：     常量值
 */
@Component  //如果想让spring帮我们@Value("${file.upload.dir}")，还需要加这个注解来提示spring
public class Constant {
    public static final String SALT = "8svbsvjkweDF,.03[}212";
    public static final String XIN_YUAN_USER = "xinYuan_user";
    public static final String XIN_YUAN_ADMIN = "xinYuan_admin";


    //@Value("${file.upload.dir}") ,在这里用@Value是无效的，因为static变量这样子是注入不了的
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")  //打上@Value的话，就可以到配置文件中去配置（利用set方法给静态变量赋值）
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    //规定排序种类的常量
    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    //规定商品上下架情况的常量
    public interface SaleStatus {
        int NOT_SALE = 0;//商品下架状态
        int SALE = 1;//商品上架状态
    }

    //规定购物车选中状态的常量
    public interface Cart {
        int UN_CHECKED = 0;//购物车未选中状态
        int CHECKED = 1;//购物车选中状态
    }


    //订单状态枚举
    public enum OrderStatusEnum {
        //以下这些相当于枚举类的常量对象（名字 加 构造方法参数便可得），也就是枚举
        CANCELED(0, "用户已取消"),
        NOT_PAID(10, "未付款"),
        PAID(20, "已付款"),
        DELIVERED(30, "已发货"),
        FINISHED(40, "交易完成");

        private String value;
        private int code;

        //构造方法
        OrderStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public static OrderStatusEnum codeOf(int code) {
            //values() 获得 该枚举类的枚举值列表
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new XinYuanException(XinYuanExceptionEnum.NO_ENUM);
        }


        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
