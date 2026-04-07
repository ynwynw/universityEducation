package com.edu.service;

import com.edu.common.PageResult;
import com.edu.entity.SysRole;
import com.edu.exception.BusinessException;
import com.edu.mapper.SysRoleMapper;
import com.edu.mapper.SysRoleMenuMapper;
import com.edu.mapper.SysUserRoleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysRoleMenuMapper roleMenuMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    public PageResult<SysRole> getPage(SysRole query, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> list = roleMapper.selectList(query);
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), list);
    }

    public List<SysRole> getList() {
        return roleMapper.selectAll();
    }

    public SysRole getById(Long id) {
        return roleMapper.selectById(id);
    }

    @Transactional
    public void add(SysRole role) {
        SysRole exist = roleMapper.selectByRoleKey(role.getRoleKey());
        if (exist != null) {
            throw new BusinessException("角色标识已存在");
        }
        role.setCreateTime(new Date());
        roleMapper.insert(role);
    }

    @Transactional
    public void update(SysRole role) {
        SysRole exist = roleMapper.selectByRoleKey(role.getRoleKey());
        if (exist != null && !exist.getId().equals(role.getId())) {
            throw new BusinessException("角色标识已存在");
        }
        role.setUpdateTime(new Date());
        roleMapper.update(role);
    }

    @Transactional
    public void delete(Long id) {
        int userCount = userRoleMapper.countByRoleId(id);
        if (userCount > 0) {
            throw new BusinessException("该角色已分配用户，无法删除");
        }
        roleMenuMapper.deleteByRoleId(id);
        roleMapper.deleteById(id);
    }

    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleMenuMapper.deleteByRoleId(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            roleMenuMapper.batchInsert(roleId, menuIds);
        }
    }

    public List<Long> getRoleMenuIds(Long roleId) {
        return roleMenuMapper.selectMenuIdsByRoleId(roleId);
    }
}
