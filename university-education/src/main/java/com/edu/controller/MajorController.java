package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduMajor;
import com.edu.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/major")
public class MajorController {
    @Autowired
    private MajorService majorService;

    @GetMapping("/page")
    @SaCheckPermission("system:major:list")
    public Result<PageResult<EduMajor>> getPage(EduMajor query, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(majorService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<EduMajor> getById(@PathVariable Long id) {
        return Result.success(majorService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<EduMajor>> getList(@RequestParam(required = false) Long collegeId) {
        return Result.success(majorService.getListByCollegeId(collegeId));
    }

    @PostMapping
    @SaCheckPermission("system:major:add")
    @Log(module = "专业管理", operation = "新增专业")
    public Result<Void> add(@RequestBody EduMajor major) {
        majorService.add(major);
        return Result.success();
    }

    @PutMapping
    @SaCheckPermission("system:major:edit")
    @Log(module = "专业管理", operation = "修改专业")
    public Result<Void> update(@RequestBody EduMajor major) {
        majorService.update(major);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:major:remove")
    @Log(module = "专业管理", operation = "删除专业")
    public Result<Void> delete(@PathVariable Long id) {
        majorService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/batch")
    @SaCheckPermission("system:major:remove")
    @Log(module = "专业管理", operation = "批量删除专业")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        majorService.batchDelete(ids);
        return Result.success();
    }
}
