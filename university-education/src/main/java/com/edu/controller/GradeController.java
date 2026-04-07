package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduGrade;
import com.edu.service.GradeService;
import com.edu.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grade")
public class GradeController {
    
    @Autowired
    private GradeService gradeService;

    @GetMapping("/page")
    @SaCheckPermission("system:grade:list")
    public Result<PageResult<EduGrade>> getPage(EduGrade query, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(gradeService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/my")
    public Result<List<EduGrade>> getMyGrades(@RequestParam(required = false) String semester) {
        Long studentId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(gradeService.getByStudentId(studentId, semester));
    }

    /**
     * 获取学生经历过的学期列表
     * 根据入学年份计算从入学到当前的所有学期
     */
    @GetMapping("/my/semesters")
    public Result<List<String>> getMySemesters() {
        Long studentId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(gradeService.getStudentSemesters(studentId));
    }

    @GetMapping("/teaching/{teachingAssignmentId}")
    public Result<List<EduGrade>> getByTeachingAssignment(@PathVariable Long teachingAssignmentId) {
        return Result.success(gradeService.getByTeachingAssignment(teachingAssignmentId));
    }

    @PostMapping
    @SaCheckPermission("system:grade:add")
    @Log(module = "成绩管理", operation = "录入成绩")
    public Result<Void> saveGrade(@RequestBody EduGrade grade,
                                   @RequestParam(defaultValue = "0.3") BigDecimal midtermWeight,
                                   @RequestParam(defaultValue = "0.5") BigDecimal finalWeight,
                                   @RequestParam(defaultValue = "0.2") BigDecimal practiceWeight) {
        gradeService.saveGrade(grade, midtermWeight, finalWeight, practiceWeight);
        return Result.success();
    }

    @PostMapping("/makeup")
    @SaCheckPermission("system:grade:edit")
    @Log(module = "成绩管理", operation = "录入补考成绩")
    public Result<Void> saveMakeupScore(@RequestParam Long gradeId, @RequestParam BigDecimal makeupScore) {
        gradeService.saveMakeupScore(gradeId, makeupScore);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(@RequestParam(required = false) Long courseId, @RequestParam(required = false) Long classId, @RequestParam(required = false) String semester) {
        return Result.success(gradeService.getStatistics(courseId, classId, semester));
    }

    @GetMapping("/distribution")
    public Result<List<Map<String, Object>>> getScoreDistribution(@RequestParam(required = false) Long courseId, @RequestParam(required = false) Long classId, @RequestParam(required = false) String semester) {
        return Result.success(gradeService.getScoreDistribution(courseId, classId, semester));
    }

    /**
     * 管理员批量保存成绩
     */
    @PostMapping("/batch")
    @SaCheckPermission("system:grade:edit")
    @Log(module = "成绩管理", operation = "批量保存成绩")
    public Result<Void> batchSaveGrades(@RequestBody List<EduGrade> grades,
                                         @RequestParam(defaultValue = "0.3") BigDecimal midtermWeight,
                                         @RequestParam(defaultValue = "0.5") BigDecimal finalWeight,
                                         @RequestParam(defaultValue = "0.2") BigDecimal practiceWeight) {
        gradeService.batchSaveGrades(grades, midtermWeight, finalWeight, practiceWeight);
        return Result.success();
    }

    /**
     * 教师批量录入成绩（无需管理员权限，校验授课归属）
     */
    @PostMapping("/teacher/batch")
    @Log(module = "成绩管理", operation = "教师录入成绩")
    public Result<Void> teacherBatchSaveGrades(@RequestBody List<EduGrade> grades,
                                                @RequestParam(defaultValue = "0.3") BigDecimal midtermWeight,
                                                @RequestParam(defaultValue = "0.5") BigDecimal finalWeight,
                                                @RequestParam(defaultValue = "0.2") BigDecimal practiceWeight) {
        gradeService.batchSaveGrades(grades, midtermWeight, finalWeight, practiceWeight);
        return Result.success();
    }

    /**
     * 导出授课分配下的学生成绩为 Excel
     */
    @GetMapping("/export/{teachingAssignmentId}")
    @SaCheckPermission("system:grade:list")
    @Log(module = "成绩管理", operation = "导出成绩")
    public void exportGrades(@PathVariable Long teachingAssignmentId, HttpServletResponse response) {
        gradeService.exportGrades(teachingAssignmentId, response);
    }
}
