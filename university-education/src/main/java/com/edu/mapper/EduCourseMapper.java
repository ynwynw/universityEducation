package com.edu.mapper;

import com.edu.entity.EduCourse;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduCourseMapper {
    EduCourse selectById(@Param("id") Long id);
    List<EduCourse> selectList(EduCourse course);
    List<EduCourse> selectElectiveCourses(@Param("semester") String semester, @Param("studentId") Long studentId);
    int insert(EduCourse course);
    int update(EduCourse course);
    int deleteById(@Param("id") Long id);
    int deleteByIds(@Param("ids") List<Long> ids);
    int checkCodeExists(@Param("courseCode") String courseCode, @Param("semester") String semester, @Param("excludeId") Long excludeId);
    int count();
}
