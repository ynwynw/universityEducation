package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduCourseSelectionConfig;
import com.edu.service.CourseSelectionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-selection-config")
public class CourseSelectionConfigController {

    @Autowired
    private CourseSelectionConfigService configService;

    @GetMapping("/page")
    @SaCheckPermission("system:course-selection-config:list")
    public Result<PageResult<EduCourseSelectionConfig>> getPage(
            EduCourseSelectionConfig query,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(configService.getPage(query, pageNum, pageSize));
    }

    /**
     * 获取所有选课配置（学生选课用）
     */
    @GetMapping("/list")
    public Result<List<EduCourseSelectionConfig>> getList() {
        return Result.success(configService.getAll());
    }

    @PostMapping
    @SaCheckPermission("system:course-selection-config:add")
    @Log(module = "选课配置", operation = "新增选课配置")
    public Result<Void> create(@RequestBody EduCourseSelectionConfig config) {
        configService.create(config);
        return Result.success();
    }

    @PutMapping("/{id}")
    @SaCheckPermission("system:course-selection-config:edit")
    @Log(module = "选课配置", operation = "修改选课配置")
    public Result<Void> update(@PathVariable Long id, @RequestBody EduCourseSelectionConfig config) {
        config.setId(id);
        configService.update(config);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:course-selection-config:remove")
    @Log(module = "选课配置", operation = "删除选课配置")
    public Result<Void> delete(@PathVariable Long id) {
        configService.delete(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    @SaCheckPermission("system:course-selection-config:edit")
    @Log(module = "选课配置", operation = "修改选课配置状态")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        configService.updateStatus(id, status);
        return Result.success();
    }
}
