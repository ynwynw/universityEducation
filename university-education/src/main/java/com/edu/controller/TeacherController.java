package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduTeacher;
import com.edu.service.TeacherService;
import com.edu.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/page")
    @SaCheckPermission("system:teacher:list")
    public Result<PageResult<EduTeacher>> getPage(EduTeacher query, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(teacherService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/college/{collegeId}")
    public Result<List<EduTeacher>> getByCollege(@PathVariable Long collegeId) {
        return Result.success(teacherService.getByCollegeId(collegeId));
    }

    @GetMapping("/{id}")
    public Result<EduTeacher> getById(@PathVariable Long id) {
        return Result.success(teacherService.getById(id));
    }

    @GetMapping("/info")
    public Result<EduTeacher> getMyInfo() {
        Long refId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(teacherService.getById(refId));
    }

    @PostMapping
    @SaCheckPermission("system:teacher:add")
    @Log(module = "教师管理", operation = "新增教师")
    public Result<Void> add(@RequestBody EduTeacher teacher) {
        teacherService.add(teacher);
        return Result.success();
    }

    @PutMapping
    @SaCheckPermission("system:teacher:edit")
    @Log(module = "教师管理", operation = "修改教师")
    public Result<Void> update(@RequestBody EduTeacher teacher) {
        teacherService.update(teacher);
        return Result.success();
    }

    @PutMapping("/info")
    @Log(module = "教师管理", operation = "修改个人信息")
    public Result<Void> updateMyInfo(@RequestBody EduTeacher teacher) {
        teacher.setId(SecurityUtil.getLoginUser().getRefId());
        teacherService.update(teacher);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:teacher:remove")
    @Log(module = "教师管理", operation = "删除教师")
    public Result<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/batch")
    @SaCheckPermission("system:teacher:remove")
    @Log(module = "教师管理", operation = "批量删除教师")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        teacherService.batchDelete(ids);
        return Result.success();
    }
}
