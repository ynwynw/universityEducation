package com.edu.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志实体
 */
public class SysOperationLog implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String module;
    private String operation;
    private String method;
    private String params;
    private String ip;
    private Long userId;
    private String username;
    private Long costTime;
    private Integer status;
    private String errorMsg;
    private Date createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getParams() { return params; }
    public void setParams(String params) { this.params = params; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getCostTime() { return costTime; }
    public void setCostTime(Long costTime) { this.costTime = costTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getErrorMsg() { return errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
