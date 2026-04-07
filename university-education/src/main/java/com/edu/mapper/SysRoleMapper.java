package com.edu.mapper;

import com.edu.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 系统角色 Mapper 接口
 */
public interface SysRoleMapper {
    
    /**
     * 根据ID查询角色
     */
    SysRole selectById(@Param("id") Long id);
    
    /**
     * 查询角色列表
     */
    List<SysRole> selectList(SysRole role);
    
    /**
     * 查询所有启用的角色
     */
    List<SysRole> selectAllEnabled();
    
    /**
     * 新增角色
     */
    int insert(SysRole role);
    
    /**
     * 更新角色
     */
    int update(SysRole role);
    
    /**
     * 删除角色
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 批量删除角色
     */
    int deleteByIds(@Param("ids") List<Long> ids);
    
    /**
     * 根据用户ID查询角色标识列表
     */
    List<String> selectRoleKeysByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
    
    /**
     * 查询所有角色
     */
    List<SysRole> selectAll();
    
    /**
     * 根据角色标识查询
     */
    SysRole selectByRoleKey(@Param("roleKey") String roleKey);
}
