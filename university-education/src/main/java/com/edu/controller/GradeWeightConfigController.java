package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.Result;
import com.edu.entity.EduGradeWeightConfig;
import com.edu.service.GradeWeightConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grade-weight-config")
public class GradeWeightConfigController {

    @Autowired
    private GradeWeightConfigService gradeWeightConfigService;

    @GetMapping("/list")
    @SaCheckPermission("system:grade-weight-config:list")
    public Result<List<EduGradeWeightConfig>> list() {
        return Result.success(gradeWeightConfigService.getAll());
    }

    @PutMapping("/{id}")
    @SaCheckPermission("system:grade-weight-config:edit")
    @Log(module = "成绩比例配置", operation = "修改配置")
    public Result<Void> update(@PathVariable Long id, @RequestBody EduGradeWeightConfig config) {
        config.setId(id);
        gradeWeightConfigService.update(config);
        return Result.success();
    }
}
