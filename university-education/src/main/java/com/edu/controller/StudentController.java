package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduStudent;
import com.edu.service.StudentService;
import com.edu.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/page")
    @SaCheckPermission("system:student:list")
    public Result<PageResult<EduStudent>> getPage(EduStudent query, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(studentService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<EduStudent> getById(@PathVariable Long id) {
        return Result.success(studentService.getById(id));
    }

    @GetMapping("/info")
    public Result<EduStudent> getMyInfo() {
        Long refId = SecurityUtil.getLoginUser().getRefId();
        return Result.success(studentService.getById(refId));
    }

    @PostMapping
    @SaCheckPermission("system:student:add")
    @Log(module = "学生管理", operation = "新增学生")
    public Result<Void> add(@RequestBody EduStudent student) {
        studentService.add(student);
        return Result.success();
    }

    @PutMapping
    @SaCheckPermission("system:student:edit")
    @Log(module = "学生管理", operation = "修改学生")
    public Result<Void> update(@RequestBody EduStudent student) {
        studentService.update(student);
        return Result.success();
    }

    @PutMapping("/info")
    @Log(module = "学生管理", operation = "修改个人信息")
    public Result<Void> updateMyInfo(@RequestBody EduStudent student) {
        student.setId(SecurityUtil.getLoginUser().getRefId());
        studentService.update(student);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:student:remove")
    @Log(module = "学生管理", operation = "删除学生")
    public Result<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/batch")
    @SaCheckPermission("system:student:remove")
    @Log(module = "学生管理", operation = "批量删除学生")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        studentService.batchDelete(ids);
        return Result.success();
    }

    @GetMapping("/import/template")
    @SaCheckPermission("system:student:list")
    public void downloadImportTemplate(HttpServletResponse response) {
        studentService.downloadImportTemplate(response);
    }

    @PostMapping("/import")
    @SaCheckPermission("system:student:add")
    @Log(module = "学生管理", operation = "批量导入学生")
    public Result<Map<String, Object>> importStudents(@RequestParam("file") MultipartFile file) {
        return Result.success(studentService.importStudents(file));
    }
}
