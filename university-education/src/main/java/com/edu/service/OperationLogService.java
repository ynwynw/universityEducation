package com.edu.service;

import com.edu.common.PageResult;
import com.edu.entity.SysOperationLog;
import com.edu.mapper.SysOperationLogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class OperationLogService {
    @Autowired
    private SysOperationLogMapper logMapper;

    public PageResult<SysOperationLog> getPage(SysOperationLog query, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysOperationLog> list = logMapper.selectList(query);
        PageInfo<SysOperationLog> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), list);
    }

    @Async
    public void saveLog(String module, String operation, String method, String params, String ip, Long userId, String username, Long costTime, Integer status, String errorMsg) {
        SysOperationLog log = new SysOperationLog();
        log.setModule(module);
        log.setOperation(operation);
        log.setMethod(method);
        log.setParams(params);
        log.setIp(ip);
        log.setUserId(userId);
        log.setUsername(username);
        log.setCostTime(costTime);
        log.setStatus(status);
        log.setErrorMsg(errorMsg);
        log.setCreateTime(new Date());
        logMapper.insert(log);
    }
}
