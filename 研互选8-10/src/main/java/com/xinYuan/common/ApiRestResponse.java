package com.xinYuan.common;


import com.xinYuan.exception.XinYuanExceptionEnum;

/**
 * 描述：     通用返回对象
 */
public class ApiRestResponse<T> {

    private Integer status;

    private String msg;

    private T data;  //data的类型不固定

    private static final int OK_CODE = 10000;  //当状态是OK的时候，状态码是10000

    private static final String OK_MSG = "SUCCESS";

    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ApiRestResponse() {
        this(OK_CODE, OK_MSG);
    }  //无参构造函数，无需传递任何信息，也就是用默认信息（请求成功），这里的this调用了两参的构造函数

    public static <T> ApiRestResponse<T> success() {
        return new ApiRestResponse<>();
    } //在成功时调用

    public static <T> ApiRestResponse<T> success(T result) {
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(result);
        return response;
    }  //成功后需要传入参数

    public static <T> ApiRestResponse<T> error(Integer code, String msg) {
        return new ApiRestResponse<>(code, msg);
    }

    //ImoocMallExceptionEnum异常枚举
    public static <T> ApiRestResponse<T> error(XinYuanExceptionEnum ex) {
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static int getOkCode() {
        return OK_CODE;
    }

    public static String getOkMsg() {
        return OK_MSG;
    }
}
