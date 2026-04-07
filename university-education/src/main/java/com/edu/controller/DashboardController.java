package com.edu.controller;

import com.edu.common.Result;
import com.edu.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private EduStudentMapper studentMapper;
    @Autowired
    private EduTeacherMapper teacherMapper;
    @Autowired
    private EduCourseMapper courseMapper;
    @Autowired
    private EduCollegeMapper collegeMapper;
    @Autowired
    private EduAnnouncementMapper announcementMapper;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("studentCount", studentMapper.count());
        stats.put("teacherCount", teacherMapper.count());
        stats.put("courseCount", courseMapper.count());
        stats.put("collegeCount", collegeMapper.count());
        return Result.success(stats);
    }

    @GetMapping("/announcements")
    public Result<List<Map<String, Object>>> getLatestAnnouncements() {
        return Result.success(announcementMapper.selectLatest(5));
    }
}
