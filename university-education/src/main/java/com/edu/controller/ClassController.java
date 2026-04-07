package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduClass;
import com.edu.entity.EduStudent;
import com.edu.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassController {
    @Autowired
    private ClassService classService;

    @GetMapping("/page")
    @SaCheckPermission("system:class:list")
    public Result<PageResult<EduClass>> getPage(EduClass query, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(classService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<EduClass> getById(@PathVariable Long id) {
        return Result.success(classService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<EduClass>> getList(@RequestParam(required = false) Long majorId) {
        return Result.success(classService.getListByMajorId(majorId));
    }

    @PostMapping
    @SaCheckPermission("system:class:add")
    @Log(module = "班级管理", operation = "新增班级")
    public Result<Void> add(@RequestBody EduClass eduClass) {
        classService.add(eduClass);
        return Result.success();
    }

    @PutMapping
    @SaCheckPermission("system:class:edit")
    @Log(module = "班级管理", operation = "修改班级")
    public Result<Void> update(@RequestBody EduClass eduClass) {
        classService.update(eduClass);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:class:remove")
    @Log(module = "班级管理", operation = "删除班级")
    public Result<Void> delete(@PathVariable Long id) {
        classService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/batch")
    @SaCheckPermission("system:class:remove")
    @Log(module = "班级管理", operation = "批量删除班级")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        classService.batchDelete(ids);
        return Result.success();
    }

    @GetMapping("/{id}/students")
    @SaCheckPermission("system:class:list")
    public Result<List<EduStudent>> getStudents(@PathVariable Long id) {
        return Result.success(classService.getStudentsByClassId(id));
    }

    @GetMapping("/{id}/export")
    @SaCheckPermission("system:class:list")
    @Log(module = "班级管理", operation = "导出学生名单")
    public void exportStudents(@PathVariable Long id, HttpServletResponse response) {
        classService.exportStudents(id, response);
    }

    @GetMapping("/import/template")
    @SaCheckPermission("system:class:list")
    public void downloadImportTemplate(HttpServletResponse response) {
        classService.downloadImportTemplate(response);
    }

    @PostMapping("/{id}/import")
    @SaCheckPermission("system:class:add")
    @Log(module = "班级管理", operation = "导入学生")
    public Result<Map<String, Object>> importStudents(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return Result.success(classService.importStudents(id, file));
    }
}
