package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduCourse;
import com.edu.service.CourseSelectionService;
import com.edu.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course-selection")
public class CourseSelectionController {
    @Autowired
    private CourseSelectionService courseSelectionService;

    @GetMapping("/available")
    public Result<List<EduCourse>> getAvailableCourses(@RequestParam(required = false) String semester) {
        Long studentId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(courseSelectionService.getAvailableCourses(semester, studentId));
    }

    @GetMapping("/my")
    public Result<List<Map<String, Object>>> getMyCourses(@RequestParam(required = false) String semester) {
        Long studentId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(courseSelectionService.getStudentSelections(studentId, semester));
    }

    @PostMapping("/select/{teachingAssignmentId}")
    @Log(module = "选课管理", operation = "学生选课")
    public Result<Void> selectCourse(@PathVariable Long teachingAssignmentId) {
        Long studentId = SecurityUtil.getLoginUser().getRefId();
        courseSelectionService.selectCourse(studentId, teachingAssignmentId);
        return Result.success();
    }

    @DeleteMapping("/cancel/{teachingAssignmentId}")
    @Log(module = "选课管理", operation = "学生退选")
    public Result<Void> cancelCourse(@PathVariable Long teachingAssignmentId) {
        Long studentId = SecurityUtil.getLoginUser().getRefId();
        courseSelectionService.cancelSelection(studentId, teachingAssignmentId);
        return Result.success();
    }

    @GetMapping("/page")
    @SaCheckPermission("system:courseSelection:list")
    public Result<PageResult<Map<String, Object>>> getPage(@RequestParam(required = false) Long courseId, @RequestParam(required = false) String semester,
            @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(courseSelectionService.getPage(courseId, semester, pageNum, pageSize));
    }
}
