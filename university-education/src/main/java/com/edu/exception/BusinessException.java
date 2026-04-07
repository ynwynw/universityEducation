package com.edu.exception;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /** 错误码 */
    private Integer code;
    
    /** 错误消息 */
    private String message;
    
    public BusinessException(String message) {
        this.code = 500;
        this.message = message;
    }
    
    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public static BusinessException error(String message) {
        return new BusinessException(message);
    }
    
    public static BusinessException error(Integer code, String message) {
        return new BusinessException(code, message);
    }
    
    /** 唯一性冲突 */
    public static BusinessException uniqueViolation(String field) {
        return new BusinessException(409, field + "已存在，请使用其他值");
    }
    
    /** 存在关联数据 */
    public static BusinessException referenceExists(String entity) {
        return new BusinessException(422, "无法删除，存在关联的" + entity);
    }
    
    /** 不在时间范围内 */
    public static BusinessException notInPeriod(String operation) {
        return new BusinessException(422, "当前不在" + operation + "时间范围内");
    }
    
    /** 字段不可修改 */
    public static BusinessException immutableField(String field) {
        return new BusinessException(422, field + "不允许修改");
    }
    
    /** 记录不可修改 */
    public static BusinessException immutableRecord() {
        return new BusinessException(422, "该记录不允许修改或删除");
    }
    
    /** 未找到数据 */
    public static BusinessException notFound(String entity) {
        return new BusinessException(404, entity + "不存在");
    }
}
