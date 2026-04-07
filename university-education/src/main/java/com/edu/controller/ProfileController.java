package com.edu.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.edu.common.Result;
import com.edu.entity.EduStudent;
import com.edu.entity.EduTeacher;
import com.edu.entity.SysUser;
import com.edu.mapper.EduStudentMapper;
import com.edu.mapper.EduTeacherMapper;
import com.edu.mapper.SysRoleMapper;
import com.edu.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心控制器
 */
@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private SysRoleMapper roleMapper;
    
    @Autowired
    private EduStudentMapper studentMapper;
    
    @Autowired
    private EduTeacherMapper teacherMapper;

    /**
     * 获取当前用户个人信息
     */
    @GetMapping("/my")
    public Result<Map<String, Object>> getMyProfile() {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userMapper.selectById(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        
        // 获取用户角色
        List<String> roles = roleMapper.selectRoleKeysByUserId(userId);
        
        if (roles.contains("student") && user.getRefId() != null && "STUDENT".equals(user.getUserType())) {
            // 学生信息
            EduStudent student = studentMapper.selectById(user.getRefId());
            if (student != null) {
                result.put("studentNo", student.getStudentNo());
                result.put("name", student.getName());
                result.put("gender", student.getGender());
                result.put("phone", student.getPhone());
                result.put("email", student.getEmail());
                result.put("avatar", student.getAvatar());
                result.put("collegeName", student.getCollegeName());
                result.put("majorName", student.getMajorName());
                result.put("className", student.getClassName());
                result.put("enrollmentYear", student.getEnrollmentYear());
            }
        } else if (roles.contains("teacher") && user.getRefId() != null && "TEACHER".equals(user.getUserType())) {
            // 教师信息
            EduTeacher teacher = teacherMapper.selectById(user.getRefId());
            if (teacher != null) {
                result.put("teacherNo", teacher.getTeacherNo());
                result.put("name", teacher.getName());
                result.put("gender", teacher.getGender());
                result.put("phone", teacher.getPhone());
                result.put("email", teacher.getEmail());
                result.put("avatar", teacher.getAvatar());
                result.put("collegeName", teacher.getCollegeName());
                result.put("title", teacher.getTitle());
                result.put("education", teacher.getEducation());
                result.put("majorStudied", teacher.getMajorStudied());
            }
        } else {
            // 管理员
            result.put("name", user.getRealName());
            result.put("phone", user.getPhone());
            result.put("email", user.getEmail());
            result.put("avatar", user.getAvatar());
        }
        
        return Result.success(result);
    }

    /**
     * 更新当前用户个人信息
     */
    @PutMapping("/my")
    public Result<Void> updateMyProfile(@RequestBody Map<String, Object> params) {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userMapper.selectById(userId);
        
        String phone = (String) params.get("phone");
        String email = (String) params.get("email");
        String avatar = (String) params.get("avatar");
        
        // 获取用户角色
        List<String> roles = roleMapper.selectRoleKeysByUserId(userId);
        
        if (roles.contains("student") && user.getRefId() != null && "STUDENT".equals(user.getUserType())) {
            EduStudent student = new EduStudent();
            student.setId(user.getRefId());
            student.setPhone(phone);
            student.setEmail(email);
            student.setAvatar(avatar);
            studentMapper.update(student);
        } else if (roles.contains("teacher") && user.getRefId() != null && "TEACHER".equals(user.getUserType())) {
            EduTeacher teacher = new EduTeacher();
            teacher.setId(user.getRefId());
            teacher.setPhone(phone);
            teacher.setEmail(email);
            teacher.setAvatar(avatar);
            teacherMapper.update(teacher);
        } else {
            // 管理员更新 sys_user 表
            SysUser updateUser = new SysUser();
            updateUser.setId(userId);
            updateUser.setPhone(phone);
            updateUser.setEmail(email);
            updateUser.setAvatar(avatar);
            userMapper.updateProfile(updateUser);
        }
        
        return Result.success();
    }
}
