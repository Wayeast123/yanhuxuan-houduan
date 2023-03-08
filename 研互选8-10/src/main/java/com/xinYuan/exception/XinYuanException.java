package com.xinYuan.exception;

/**
 * 描述：     统一异常 ,可以直接当做一个异常抛出
 */
public class XinYuanException extends RuntimeException {
    //Exception中除了RuntimeException，都是“受检查异常”,所以继承了RuntimeException就不受检查了，
    // 出错后会在控制台得知，编译器不要求必须提供异常处理器或者抛出去

    private final Integer code;
    private final String message;

    public XinYuanException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public XinYuanException(XinYuanExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }


}
