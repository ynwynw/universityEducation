package com.edu.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户角色关联Mapper接口
 */
public interface SysUserRoleMapper {
    
    /**
     * 新增用户角色关联
     */
    int insert(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    /**
     * 批量新增用户角色关联
     */
    int batchInsert(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    
    /**
     * 删除用户的所有角色关联
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 删除角色的所有用户关联
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 查询角色关联的用户数量
     */
    int countByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 查询用户的角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
}
