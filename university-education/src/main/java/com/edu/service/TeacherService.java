package com.edu.service;

import cn.hutool.core.util.RandomUtil;
import com.edu.common.PageResult;
import com.edu.entity.EduTeacher;
import com.edu.entity.SysUser;
import com.edu.exception.BusinessException;
import com.edu.mapper.EduTeacherMapper;
import com.edu.mapper.SysUserMapper;
import com.edu.mapper.SysUserRoleMapper;
import com.edu.util.Md5Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class TeacherService {
    @Autowired
    private EduTeacherMapper teacherMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    public PageResult<EduTeacher> getPage(EduTeacher query, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduTeacher> list = teacherMapper.selectList(query);
        PageInfo<EduTeacher> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), list);
    }

    public EduTeacher getById(Long id) { return teacherMapper.selectById(id); }
    public EduTeacher getByTeacherNo(String teacherNo) { return teacherMapper.selectByTeacherNo(teacherNo); }
    public List<EduTeacher> getByCollegeId(Long collegeId) { return teacherMapper.selectByCollegeId(collegeId); }

    @Transactional
    public void add(EduTeacher teacher) {
        if (teacherMapper.checkTeacherNoExists(teacher.getTeacherNo(), null) > 0) {
            throw new BusinessException("工号已存在");
        }
        if (teacher.getStatus() == null) teacher.setStatus(1);
        teacherMapper.insert(teacher);
        // 创建用户账号
        SysUser user = new SysUser();
        user.setUsername(teacher.getTeacherNo());
        String salt = RandomUtil.randomString(6);
        user.setSalt(salt);
        user.setPassword(Md5Util.encrypt("123456", salt));
        user.setRealName(teacher.getName());
        user.setUserType("TEACHER");
        user.setRefId(teacher.getId());
        user.setStatus(1);
        userMapper.insert(user);
        userRoleMapper.batchInsert(user.getId(), Arrays.asList(2L)); // 教师角色
    }

    @Transactional
    public void update(EduTeacher teacher) {
        EduTeacher old = teacherMapper.selectById(teacher.getId());
        if (old == null) throw new BusinessException("教师不存在");
        teacher.setTeacherNo(null); // 工号不可修改
        teacherMapper.update(teacher);
    }

    @Transactional
    public void delete(Long id) { teacherMapper.deleteById(id); }

    @Transactional
    public void batchDelete(List<Long> ids) { teacherMapper.deleteByIds(ids); }
}
