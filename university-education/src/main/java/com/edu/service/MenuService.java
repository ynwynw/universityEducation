package com.edu.service;

import com.edu.entity.SysMenu;
import com.edu.exception.BusinessException;
import com.edu.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Autowired
    private SysMenuMapper menuMapper;

    public List<SysMenu> getTree() {
        List<SysMenu> all = menuMapper.selectAll();
        return buildTree(all, 0L);
    }

    public List<SysMenu> getUserMenus(Long userId) {
        List<SysMenu> menus = menuMapper.selectByUserId(userId);
        return buildTree(menus, 0L);
    }

    private List<SysMenu> buildTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(m -> Objects.equals(m.getParentId(), parentId))
                .peek(m -> m.setChildren(buildTree(menus, m.getId())))
                .sorted(Comparator.comparingInt(m -> m.getSort() == null ? 0 : m.getSort()))
                .collect(Collectors.toList());
    }

    public SysMenu getById(Long id) {
        return menuMapper.selectById(id);
    }

    @Transactional
    public void add(SysMenu menu) {
        menu.setCreateTime(new Date());
        menuMapper.insert(menu);
    }

    @Transactional
    public void update(SysMenu menu) {
        menu.setUpdateTime(new Date());
        menuMapper.update(menu);
    }

    @Transactional
    public void delete(Long id) {
        int childCount = menuMapper.countByParentId(id);
        if (childCount > 0) {
            throw new BusinessException("存在子菜单，无法删除");
        }
        menuMapper.deleteById(id);
    }
}
