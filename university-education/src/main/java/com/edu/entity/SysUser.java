package com.edu.entity;

import com.edu.common.BaseEntity;
import java.io.Serializable;

/**
 * 系统用户实体
 */
public class SysUser extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 用户ID */
    private Long id;
    
    /** 用户名 */
    private String username;
    
    /** 密码 */
    private String password;
    
    /** 盐值 */
    private String salt;
    
    /** 真实姓名 */
    private String realName;
    
    /** 用户类型: ADMIN-管理员, TEACHER-教师, STUDENT-学生 */
    private String userType;
    
    /** 关联ID (教师ID或学生ID) */
    private Long refId;
    
    /** 状态: 0-禁用, 1-启用 */
    private Integer status;
    
    /** 头像 */
    private String avatar;
    
    /** 手机号 */
    private String phone;
    
    /** 邮箱 */
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
