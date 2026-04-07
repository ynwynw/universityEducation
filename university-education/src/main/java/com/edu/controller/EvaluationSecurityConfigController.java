package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduEvaluationSecurityConfig;
import com.edu.service.EvaluationSecurityConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluation-security-config")
public class EvaluationSecurityConfigController {

    @Autowired
    private EvaluationSecurityConfigService configService;

    @GetMapping("/page")
    @SaCheckPermission("system:evaluationSecurityConfig:list")
    public Result<PageResult<EduEvaluationSecurityConfig>> getPage(EduEvaluationSecurityConfig query,
            @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(configService.getPage(query, pageNum, pageSize));
    }

    @PostMapping
    @SaCheckPermission("system:evaluationSecurityConfig:add")
    @Log(module = "评教安全配置", operation = "新增配置")
    public Result<Void> create(@RequestBody EduEvaluationSecurityConfig config) {
        configService.create(config);
        return Result.success();
    }

    @PutMapping("/{id}")
    @SaCheckPermission("system:evaluationSecurityConfig:edit")
    @Log(module = "评教安全配置", operation = "修改配置")
    public Result<Void> update(@PathVariable Long id, @RequestBody EduEvaluationSecurityConfig config) {
        config.setId(id);
        configService.update(config);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:evaluationSecurityConfig:remove")
    @Log(module = "评教安全配置", operation = "删除配置")
    public Result<Void> delete(@PathVariable Long id) {
        configService.delete(id);
        return Result.success();
    }
}
