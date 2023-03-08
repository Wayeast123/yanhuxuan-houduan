package com.xinYuan.util;

import com.xinYuan.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：MD5工具，MD5Utils是一种哈希算法，它会把字符串通过哈希的方式转化为另外一个难以辨认的字符串（无法反推，本质是一种哈希运算）
 */

public class MD5Utils {
    //工具类用static来修饰方法，以便其它类调用
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");  //传入算法MD5，得到对应的MD5工具对象
        //md5.digest(strValue.getBytes());  //md5.digest()接收的类型是Byte
        //对得到的字符串进行Base64的转码，方便存储 ; Constant.SALT 加盐，进一步加大破解难度（盐值不能过于简单）
        return Base64.encodeBase64String(md5.digest((strValue + Constant.SALT).getBytes()));
    }

    //用这个方法测试生成的MD5的值
    public static void main(String[] args) {
        String md5 = null;
        try {
            md5 = getMD5Str("1234");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(md5);
    }
}
