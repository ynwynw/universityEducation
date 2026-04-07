package com.edu.service;

import com.edu.common.PageResult;
import com.edu.entity.EduCourseCollegeLimit;
import com.edu.exception.BusinessException;
import com.edu.mapper.EduTeachingAssignmentMapper;
import com.edu.mapper.EduCourseMapper;
import com.edu.mapper.EduTeacherMapper;
import com.edu.mapper.EduCourseCollegeLimitMapper;
import com.edu.entity.EduCourse;
import com.edu.entity.EduTeacher;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class TeachingAssignmentService {
    @Autowired
    private EduTeachingAssignmentMapper assignmentMapper;
    @Autowired
    private EduCourseMapper courseMapper;
    @Autowired
    private EduTeacherMapper teacherMapper;
    @Autowired
    private EduCourseCollegeLimitMapper collegeLimitMapper;

    public PageResult<Map<String, Object>> getPage(Long courseId, Long teacherId, Long classId, String semester, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = assignmentMapper.selectList(courseId, teacherId, classId, semester);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), list);
    }

    public List<Map<String, Object>> getByTeacherId(Long teacherId, String semester) {
        return assignmentMapper.selectByTeacherId(teacherId, semester);
    }

    @Transactional
    public void add(Long courseId, Long teacherId, Long classId, String semester) {
        // 检查是否已存在
        if (assignmentMapper.checkExists(courseId, teacherId, classId) > 0) {
            throw new BusinessException("该授课分配已存在");
        }
        // 检查教师学院限制
        EduCourse course = courseMapper.selectById(courseId);
        EduTeacher teacher = teacherMapper.selectById(teacherId);
        if (course != null && teacher != null) {
            String courseType = course.getCourseType();
            // 专业必修和专业选修课需要本学院教师
            if ("MAJOR_REQUIRED".equals(courseType) || "MAJOR_ELECTIVE".equals(courseType)) {
                if (!course.getCollegeId().equals(teacher.getCollegeId())) {
                    throw new BusinessException("专业课只能分配给本学院教师");
                }
            }
            // 公共课检查学院限制配置
            if ("PUBLIC_REQUIRED".equals(courseType) || "PUBLIC_ELECTIVE".equals(courseType)) {
                List<EduCourseCollegeLimit> limits = collegeLimitMapper.selectByCourseCode(course.getCourseCode());
                if (limits != null && !limits.isEmpty()) {
                    boolean allowed = limits.stream().anyMatch(l -> l.getCollegeId().equals(teacher.getCollegeId()));
                    if (!allowed) {
                        throw new BusinessException("该教师所在学院不在该课程的授课学院范围内");
                    }
                }
            }
        }
        assignmentMapper.insert(courseId, teacherId, classId, semester);
    }

    @Transactional
    public void delete(Long id) { assignmentMapper.deleteById(id); }

    @Transactional
    public void batchDelete(List<Long> ids) { assignmentMapper.deleteByIds(ids); }
}
