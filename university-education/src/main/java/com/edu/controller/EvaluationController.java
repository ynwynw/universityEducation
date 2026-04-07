package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.service.EvaluationService;
import com.edu.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {
    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/pending")
    public Result<List<Map<String, Object>>> getPendingCourses() {
        Long studentId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(evaluationService.getPendingCourses(studentId));
    }

    @PostMapping("/submit")
    @Log(module = "评教管理", operation = "学生提交评教")
    public Result<Void> submitEvaluation(@RequestBody Map<String, Object> evaluation) {
        Long studentId = SecurityUtil.getLoginUser().getRefId();
        evaluationService.submitEvaluation(studentId, evaluation);
        return Result.success();
    }

    @GetMapping("/teacher/pending")
    public Result<List<Map<String, Object>>> getPendingTeachers() {
        Long teacherId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(evaluationService.getPendingTeachers(teacherId));
    }

    @PostMapping("/teacher/submit")
    @Log(module = "评教管理", operation = "教师提交互评")
    public Result<Void> submitTeacherEvaluation(@RequestBody Map<String, Object> evaluation) {
        Long teacherId = SecurityUtil.getLoginUser().getRefId();
        evaluationService.submitTeacherEvaluation(teacherId, evaluation);
        return Result.success();
    }

    @GetMapping("/my-result")
    public Result<Map<String, Object>> getMyResult(@RequestParam(required = false) String semester) {
        Long teacherId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(evaluationService.getTeacherResult(teacherId, semester));
    }

    @GetMapping("/page")
    @SaCheckPermission("system:evaluation:list")
    public Result<PageResult<Map<String, Object>>> getPage(@RequestParam(required = false) Long teacherId, @RequestParam(required = false) String semester,
            @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(evaluationService.getPage(teacherId, semester, pageNum, pageSize));
    }

    @GetMapping("/statistics")
    @SaCheckPermission("system:evaluation:list")
    public Result<Map<String, Object>> getStatistics(@RequestParam(required = false) Long collegeId, @RequestParam String semester) {
        return Result.success(evaluationService.getStatistics(collegeId, semester));
    }
}
