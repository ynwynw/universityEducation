package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.service.TeachingAssignmentService;
import com.edu.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teaching-assignment")
public class TeachingAssignmentController {
    @Autowired
    private TeachingAssignmentService assignmentService;

    @GetMapping("/page")
    @SaCheckPermission("system:teachingAssignment:list")
    public Result<PageResult<Map<String, Object>>> getPage(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String semester,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(assignmentService.getPage(courseId, teacherId, classId, semester, pageNum, pageSize));
    }

    @GetMapping("/my")
    public Result<List<Map<String, Object>>> getMyAssignments(@RequestParam(required = false) String semester) {
        Long teacherId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(assignmentService.getByTeacherId(teacherId, semester));
    }

    @PostMapping
    @SaCheckPermission("system:teachingAssignment:add")
    @Log(module = "授课管理", operation = "新增授课分配")
    public Result<Void> add(@RequestBody Map<String, Object> params) {
        Long courseId = Long.valueOf(params.get("courseId").toString());
        Long teacherId = Long.valueOf(params.get("teacherId").toString());
        Long classId = Long.valueOf(params.get("classId").toString());
        String semester = (String) params.get("semester");
        assignmentService.add(courseId, teacherId, classId, semester);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:teachingAssignment:remove")
    @Log(module = "授课管理", operation = "删除授课分配")
    public Result<Void> delete(@PathVariable Long id) {
        assignmentService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/batch")
    @SaCheckPermission("system:teachingAssignment:remove")
    @Log(module = "授课管理", operation = "批量删除授课分配")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        assignmentService.batchDelete(ids);
        return Result.success();
    }
}
