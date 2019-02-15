package com.duckdream.superbuy.exception;

import com.duckdream.superbuy.result.CodeMsg;
import com.duckdream.superbuy.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        if(e instanceof GlobalException) {
            //全局拦截器器拦截异常
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCm());
        } else if(e instanceof BindException) {
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors(); //获取Error对象列表
            ObjectError error = errors.get(0); //得到第一个Error对象
            String msg = error.getDefaultMessage(); //得到error信息
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg)); //把msg传到绑定异常中
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
