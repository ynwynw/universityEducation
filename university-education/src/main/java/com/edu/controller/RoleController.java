package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.SysRole;
import com.edu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/page")
    @SaCheckPermission("system:role:list")
    public Result<PageResult<SysRole>> getPage(SysRole query, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(roleService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/list")
    public Result<List<SysRole>> getList() {
        return Result.success(roleService.getList());
    }

    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    @PostMapping
    @SaCheckPermission("system:role:add")
    @Log(module = "角色管理", operation = "新增角色")
    public Result<Void> add(@RequestBody SysRole role) {
        roleService.add(role);
        return Result.success();
    }

    @PutMapping
    @SaCheckPermission("system:role:edit")
    @Log(module = "角色管理", operation = "修改角色")
    public Result<Void> update(@RequestBody SysRole role) {
        roleService.update(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:role:remove")
    @Log(module = "角色管理", operation = "删除角色")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }

    @PutMapping("/{roleId}/menus")
    @SaCheckPermission("system:role:edit")
    @Log(module = "角色管理", operation = "分配菜单权限")
    public Result<Void> assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        roleService.assignMenus(roleId, menuIds);
        return Result.success();
    }

    @GetMapping("/{roleId}/menus")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        return Result.success(roleService.getRoleMenuIds(roleId));
    }
}
