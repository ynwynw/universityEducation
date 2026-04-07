package com.edu.util;

import cn.dev33.satoken.stp.StpUtil;
import com.edu.entity.SysUser;

/**
 * 安全工具类
 */
public class SecurityUtil {
    
    /**
     * 获取当前登录用户ID
     */
    public static Long getUserId() {
        return StpUtil.getLoginIdAsLong();
    }
    
    /**
     * 获取当前登录用户名
     */
    public static String getUsername() {
        SysUser user = (SysUser) StpUtil.getSession().get("user");
        return user != null ? user.getUsername() : null;
    }
    
    /**
     * 获取当前登录用户
     */
    public static SysUser getLoginUser() {
        return (SysUser) StpUtil.getSession().get("user");
    }
    
    /**
     * 获取当前用户类型
     */
    public static String getUserType() {
        SysUser user = getLoginUser();
        return user != null ? user.getUserType() : null;
    }
    
    /**
     * 是否为管理员
     */
    public static boolean isAdmin() {
        return "ADMIN".equals(getUserType());
    }
    
    /**
     * 是否为教师
     */
    public static boolean isTeacher() {
        return "TEACHER".equals(getUserType());
    }
    
    /**
     * 是否为学生
     */
    public static boolean isStudent() {
        return "STUDENT".equals(getUserType());
    }
}
