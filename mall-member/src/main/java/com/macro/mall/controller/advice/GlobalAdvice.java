package com.macro.mall.controller.advice;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author superUser
 * @Date 2021/6/13
 */
@RestControllerAdvice
public class GlobalAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public CommonResult exceptionHandler(Exception exception){
        if(exception instanceof BusinessException){
            return CommonResult.failed("请稍后重试："+exception.getMessage());
        }else if(exception instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException manve = (MethodArgumentNotValidException)exception;
            return CommonResult.failed("入参校验失败："+manve.getBindingResult().getFieldError().getDefaultMessage());
        }
        return CommonResult.failed();
    }
}
