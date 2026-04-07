package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduAnnouncement;
import com.edu.service.AnnouncementService;
import com.edu.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/page")
    @SaCheckPermission("system:announcement:list")
    public Result<PageResult<EduAnnouncement>> getPage(EduAnnouncement query, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(announcementService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<EduAnnouncement> getById(@PathVariable Long id) {
        announcementService.markAsRead(id, SecurityUtil.getUserId());
        return Result.success(announcementService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<EduAnnouncement>> getList() {
        String userType = SecurityUtil.getUserType();
        String targetType = "ADMIN".equals(userType) ? "ALL" : userType;
        return Result.success(announcementService.getByTargetType(targetType, SecurityUtil.getUserId()));
    }

    @GetMapping("/unread/count")
    public Result<Integer> getUnreadCount() {
        String userType = SecurityUtil.getUserType();
        String targetType = "ADMIN".equals(userType) ? "ALL" : userType;
        return Result.success(announcementService.countUnread(SecurityUtil.getUserId(), targetType));
    }

    @PostMapping
    @SaCheckPermission("system:announcement:add")
    @Log(module = "公告管理", operation = "新增公告")
    public Result<Void> add(@RequestBody EduAnnouncement announcement) {
        announcementService.add(announcement);
        return Result.success();
    }

    @PutMapping
    @SaCheckPermission("system:announcement:edit")
    @Log(module = "公告管理", operation = "修改公告")
    public Result<Void> update(@RequestBody EduAnnouncement announcement) {
        announcementService.update(announcement);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:announcement:remove")
    @Log(module = "公告管理", operation = "删除公告")
    public Result<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/batch")
    @SaCheckPermission("system:announcement:remove")
    @Log(module = "公告管理", operation = "批量删除公告")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        announcementService.batchDelete(ids);
        return Result.success();
    }
}
