package com.edu.entity;

import com.edu.common.BaseEntity;
import java.io.Serializable;
import java.util.List;

/**
 * 系统菜单实体
 */
public class SysMenu extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 菜单ID */
    private Long id;
    
    /** 菜单名称 */
    private String menuName;
    
    /** 父菜单ID */
    private Long parentId;
    
    /** 显示顺序 */
    private Integer sort;
    
    /** 路由地址 */
    private String path;
    
    /** 组件路径 */
    private String component;
    
    /** 权限标识 */
    private String perms;
    
    /** 菜单图标 */
    private String icon;
    
    /** 菜单类型: M-目录, C-菜单, F-按钮 */
    private String menuType;
    
    /** 是否显示: 0-隐藏, 1-显示 */
    private Integer visible;
    
    /** 状态: 0-禁用, 1-启用 */
    private Integer status;
    
    /** 子菜单 */
    private List<SysMenu> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }
}
