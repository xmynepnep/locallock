package com.tzy.locallock.advice;

import com.tzy.locallock.exception.AspectException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public String errorHandler(Exception e){
        if(e instanceof AspectException){
            return ((AspectException) e).getMsg();
        }
        else {
            return "其他异常";
        }
    }

}
