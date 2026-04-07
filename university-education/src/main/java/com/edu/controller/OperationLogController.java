package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.SysOperationLog;
import com.edu.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
public class OperationLogController {
    @Autowired
    private OperationLogService logService;

    @GetMapping("/page")
    @SaCheckPermission("system:log:list")
    public Result<PageResult<SysOperationLog>> getPage(SysOperationLog query, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(logService.getPage(query, pageNum, pageSize));
    }
}
