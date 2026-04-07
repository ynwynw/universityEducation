package com.edu.mapper;

import com.edu.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 系统用户Mapper接口
 */
public interface SysUserMapper {
    
    /**
     * 根据ID查询用户
     */
    SysUser selectById(@Param("id") Long id);
    
    /**
     * 根据用户名查询用户
     */
    SysUser selectByUsername(@Param("username") String username);
    
    /**
     * 查询用户列表
     */
    List<SysUser> selectList(SysUser user);
    
    /**
     * 新增用户
     */
    int insert(SysUser user);
    
    /**
     * 更新用户
     */
    int update(SysUser user);
    
    /**
     * 更新密码
     */
    int updatePassword(SysUser user);
    
    /**
     * 删除用户
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 批量删除用户
     */
    int deleteByIds(@Param("ids") List<Long> ids);
    
    /**
     * 检查用户名是否存在
     */
    int checkUsernameExists(@Param("username") String username, @Param("excludeId") Long excludeId);
    
    /**
     * 更新用户个人信息
     */
    int updateProfile(SysUser user);
}
