package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.Result;
import com.edu.entity.SysMenu;
import com.edu.service.MenuService;
import com.edu.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/tree")
    @SaCheckPermission("system:menu:list")
    public Result<List<SysMenu>> getTree() {
        return Result.success(menuService.getTree());
    }

    @GetMapping("/user")
    public Result<List<SysMenu>> getUserMenus() {
        Long userId = SecurityUtil.getUserId();
        return Result.success(menuService.getUserMenus(userId));
    }

    @GetMapping("/{id}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        return Result.success(menuService.getById(id));
    }

    @PostMapping
    @SaCheckPermission("system:menu:add")
    @Log(module = "菜单管理", operation = "新增菜单")
    public Result<Void> add(@RequestBody SysMenu menu) {
        menuService.add(menu);
        return Result.success();
    }

    @PutMapping
    @SaCheckPermission("system:menu:edit")
    @Log(module = "菜单管理", operation = "修改菜单")
    public Result<Void> update(@RequestBody SysMenu menu) {
        menuService.update(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:menu:remove")
    @Log(module = "菜单管理", operation = "删除菜单")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success();
    }
}
