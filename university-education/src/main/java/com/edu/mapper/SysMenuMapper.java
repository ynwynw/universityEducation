package com.edu.mapper;

import com.edu.entity.SysMenu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 系统菜单 Mapper 接口
 */
public interface SysMenuMapper {
    
    /**
     * 根据ID查询菜单
     */
    SysMenu selectById(@Param("id") Long id);
    
    /**
     * 查询菜单列表
     */
    List<SysMenu> selectList(SysMenu menu);
    
    /**
     * 查询所有菜单
     */
    List<SysMenu> selectAll();
    
    /**
     * 新增菜单
     */
    int insert(SysMenu menu);
    
    /**
     * 更新菜单
     */
    int update(SysMenu menu);
    
    /**
     * 删除菜单
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据用户ID查询权限标识列表
     */
    List<String> selectPermsByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询菜单列表
     */
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID查询菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 查询子菜单数量
     */
    int countByParentId(@Param("parentId") Long parentId);
    
    /**
     * 根据用户ID查询菜单
     */
    List<SysMenu> selectByUserId(@Param("userId") Long userId);
}
