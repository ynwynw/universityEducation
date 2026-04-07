package com.edu.entity;

import com.edu.common.BaseEntity;
import java.io.Serializable;

/**
 * 系统角色实体
 */
public class SysRole extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 角色ID */
    private Long id;
    
    /** 角色名称 */
    private String roleName;
    
    /** 角色标识 */
    private String roleKey;
    
    /** 显示顺序 */
    private Integer sort;
    
    /** 状态: 0-禁用, 1-启用 */
    private Integer status;
    
    /** 备注 */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
