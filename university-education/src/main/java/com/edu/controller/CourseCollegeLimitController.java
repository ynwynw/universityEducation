package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduCourseCollegeLimit;
import com.edu.service.CourseCollegeLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-college-limit")
public class CourseCollegeLimitController {

    @Autowired
    private CourseCollegeLimitService limitService;

    @GetMapping("/page")
    @SaCheckPermission("system:courseCollegeLimit:list")
    public Result<PageResult<EduCourseCollegeLimit>> getPage(EduCourseCollegeLimit query,
            @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(limitService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/by-course/{courseCode}")
    public Result<List<EduCourseCollegeLimit>> getByCourseCode(@PathVariable String courseCode) {
        return Result.success(limitService.getByCourseCode(courseCode));
    }

    @PostMapping
    @SaCheckPermission("system:courseCollegeLimit:add")
    @Log(module = "公共课学院限制", operation = "新增限制")
    public Result<Void> create(@RequestBody EduCourseCollegeLimit limit) {
        limitService.create(limit);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:courseCollegeLimit:remove")
    @Log(module = "公共课学院限制", operation = "删除限制")
    public Result<Void> delete(@PathVariable Long id) {
        limitService.delete(id);
        return Result.success();
    }
}
