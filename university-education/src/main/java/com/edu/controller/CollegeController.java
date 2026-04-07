package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduCollege;
import com.edu.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学院管理控制器
 */
@RestController
@RequestMapping("/college")
public class CollegeController {
    
    @Autowired
    private CollegeService collegeService;
    
    /**
     * 分页查询学院
     */
    @GetMapping("/page")
    @SaCheckPermission("system:college:list")
    public Result<PageResult<EduCollege>> getPage(
            EduCollege query,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(collegeService.getPage(query, pageNum, pageSize));
    }
    
    /**
     * 获取所有学院（下拉框）
     */
    @GetMapping("/list")
    public Result<List<EduCollege>> getList() {
        return Result.success(collegeService.getAll());
    }
    
    /**
     * 根据ID查询学院
     */
    @GetMapping("/{id}")
    @SaCheckPermission("system:college:query")
    public Result<EduCollege> getById(@PathVariable Long id) {
        return Result.success(collegeService.getById(id));
    }
    
    /**
     * 新增学院
     */
    @PostMapping
    @SaCheckPermission("system:college:add")
    @Log(module = "学院管理", operation = "新增学院")
    public Result<Void> add(@RequestBody EduCollege college) {
        collegeService.add(college);
        return Result.success();
    }
    
    /**
     * 修改学院
     */
    @PutMapping
    @SaCheckPermission("system:college:edit")
    @Log(module = "学院管理", operation = "修改学院")
    public Result<Void> update(@RequestBody EduCollege college) {
        collegeService.update(college);
        return Result.success();
    }
    
    /**
     * 删除学院
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("system:college:remove")
    @Log(module = "学院管理", operation = "删除学院")
    public Result<Void> delete(@PathVariable Long id) {
        collegeService.delete(id);
        return Result.success();
    }
    
    /**
     * 批量删除学院
     */
    @DeleteMapping("/batch")
    @SaCheckPermission("system:college:remove")
    @Log(module = "学院管理", operation = "批量删除学院")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        collegeService.batchDelete(ids);
        return Result.success();
    }
}
