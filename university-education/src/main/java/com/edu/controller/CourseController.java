package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduCourse;
import com.edu.service.CourseService;
import com.edu.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/page")
    @SaCheckPermission("system:course:list")
    public Result<PageResult<EduCourse>> getPage(EduCourse query, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(courseService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<EduCourse> getById(@PathVariable Long id) {
        return Result.success(courseService.getById(id));
    }

    @GetMapping("/elective")
    public Result<List<EduCourse>> getElectiveCourses(@RequestParam String semester) {
        Long studentId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(courseService.getElectiveCourses(semester, studentId));
    }

    @PostMapping
    @SaCheckPermission("system:course:add")
    @Log(module = "课程管理", operation = "新增课程")
    public Result<Void> add(@RequestBody EduCourse course) {
        courseService.add(course);
        return Result.success();
    }

    @PutMapping
    @SaCheckPermission("system:course:edit")
    @Log(module = "课程管理", operation = "修改课程")
    public Result<Void> update(@RequestBody EduCourse course) {
        courseService.update(course);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:course:remove")
    @Log(module = "课程管理", operation = "删除课程")
    public Result<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/batch")
    @SaCheckPermission("system:course:remove")
    @Log(module = "课程管理", operation = "批量删除课程")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        courseService.batchDelete(ids);
        return Result.success();
    }
}
