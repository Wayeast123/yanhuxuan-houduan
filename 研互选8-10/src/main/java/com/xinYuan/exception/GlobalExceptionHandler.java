package com.xinYuan.exception;

import com.xinYuan.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：     处理统一异常的handler(异常处理器),将各种类型的异常都按一种格式输出
 */
@ControllerAdvice  //用于拦截异常
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);  //创建日志对象

    //拦截系统错误异常（异常处理器）
    @ExceptionHandler(Exception.class)  //注解说明要处理的异常类型
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("Default Exception: ", e);  //写进异常日志
        return ApiRestResponse.error(XinYuanExceptionEnum.SYSTEM_ERROR);
    }

    //拦截业务错误异常
    @ExceptionHandler(XinYuanException.class)
    @ResponseBody
    public Object handleImoocMallException(XinYuanException e) {
        log.error("ImoocMallException: ", e);   //不再是默认异常，而是我们传进什么异常就是什么异常
        return ApiRestResponse.error(e.getCode(), e.getMessage());
    }

    //处理方法参数不合规的情况
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e);
        return handleBindingResult(e.getBindingResult());
    }

    //把异常 处理为 对外暴露的提示
    private ApiRestResponse handleBindingResult(BindingResult result) {
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError objectError : allErrors) {
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }
        if (list.size() == 0) {
            return ApiRestResponse.error(XinYuanExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse
                .error(XinYuanExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
    }
}
