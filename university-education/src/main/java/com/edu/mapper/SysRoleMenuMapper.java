package com.edu.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 角色菜单关联 Mapper 接口
 */
public interface SysRoleMenuMapper {
    
    /**
     * 新增角色菜单关联
     */
    int insert(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
    
    /**
     * 批量新增角色菜单关联
     */
    int batchInsert(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
    
    /**
     * 删除角色的所有菜单关联
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 删除菜单的所有角色关联
     */
    int deleteByMenuId(@Param("menuId") Long menuId);
    
    /**
     * 查询菜单关联的角色数量
     */
    int countByMenuId(@Param("menuId") Long menuId);
    
    /**
     * 查询角色的菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
