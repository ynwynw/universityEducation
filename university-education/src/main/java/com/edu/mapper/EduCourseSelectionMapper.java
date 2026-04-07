package com.edu.mapper;

import com.edu.entity.EduCourse;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface EduCourseSelectionMapper {
    List<EduCourse> selectAvailableCourses(@Param("semester") String semester, @Param("studentId") Long studentId);
    List<Map<String, Object>> selectByStudentId(@Param("studentId") Long studentId, @Param("semester") String semester);
    int insert(@Param("studentId") Long studentId, @Param("teachingAssignmentId") Long teachingAssignmentId);
    int delete(@Param("studentId") Long studentId, @Param("teachingAssignmentId") Long teachingAssignmentId);
    int checkExists(@Param("studentId") Long studentId, @Param("teachingAssignmentId") Long teachingAssignmentId);
    Map<String, Object> selectAssignmentInfo(@Param("teachingAssignmentId") Long teachingAssignmentId);
    List<Map<String, Object>> selectPage(@Param("courseId") Long courseId, @Param("semester") String semester);
}
