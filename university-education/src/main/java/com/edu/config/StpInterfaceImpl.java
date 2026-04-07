package com.edu.config;

import cn.dev33.satoken.stp.StpInterface;
import com.edu.mapper.SysMenuMapper;
import com.edu.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token权限认证接口实现
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    
    @Autowired(required = false)
    private SysRoleMapper roleMapper;
    
    @Autowired(required = false)
    private SysMenuMapper menuMapper;
    
    /**
     * 获取用户权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        // 查询用户角色，管理员拥有所有权限
        if (roleMapper != null) {
            List<String> roles = roleMapper.selectRoleKeysByUserId(userId);
            if (roles != null && roles.contains("admin")) {
                List<String> allPerms = new ArrayList<>();
                allPerms.add("*");
                return allPerms;
            }
        }
        if (menuMapper != null) {
            List<String> perms = menuMapper.selectPermsByUserId(userId);
            return perms != null ? perms : new ArrayList<>();
        }
        return new ArrayList<>();
    }
    
    /**
     * 获取用户角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        if (roleMapper != null) {
            List<String> roles = roleMapper.selectRoleKeysByUserId(userId);
            return roles != null ? roles : new ArrayList<>();
        }
        return new ArrayList<>();
    }
}
