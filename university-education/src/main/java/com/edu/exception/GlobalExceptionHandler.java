package com.edu.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.edu.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生业务异常: {}", request.getRequestURI(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public Result<?> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        log.error("请求地址'{}',未登录: {}", request.getRequestURI(), e.getMessage());
        return Result.error(401, "登录已过期，请重新登录");
    }
    
    /**
     * 无权限异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<?> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        log.error("请求地址'{}',无权限: {}", request.getRequestURI(), e.getMessage());
        return Result.error(403, "您没有权限执行此操作");
    }
    
    /**
     * 无角色异常
     */
    @ExceptionHandler(NotRoleException.class)
    public Result<?> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        log.error("请求地址'{}',无角色: {}", request.getRequestURI(), e.getMessage());
        return Result.error(403, "您没有权限执行此操作");
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("请求地址'{}',参数校验失败: {}", request.getRequestURI(), e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError error : fieldErrors) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return Result.error(400, sb.toString());
    }
    
    /**
     * 绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e, HttpServletRequest request) {
        log.error("请求地址'{}',绑定异常: {}", request.getRequestURI(), e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError error : fieldErrors) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return Result.error(400, sb.toString());
    }
    
    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生系统异常: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.error("系统异常，请联系管理员");
    }
}
