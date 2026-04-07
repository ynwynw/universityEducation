package com.edu.mapper;

import com.edu.entity.EduGrade;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface EduGradeMapper {
    EduGrade selectById(@Param("id") Long id);
    List<EduGrade> selectList(EduGrade grade);
    List<EduGrade> selectByStudentId(@Param("studentId") Long studentId, @Param("semester") String semester);
    List<EduGrade> selectByTeachingAssignment(@Param("teachingAssignmentId") Long teachingAssignmentId);
    int insert(EduGrade grade);
    int update(EduGrade grade);
    int batchInsert(@Param("list") List<EduGrade> grades);
    int batchUpdate(@Param("list") List<EduGrade> grades);
    Map<String, Object> selectStatistics(@Param("courseId") Long courseId, @Param("classId") Long classId, @Param("semester") String semester);
    List<Map<String, Object>> selectScoreDistribution(@Param("courseId") Long courseId, @Param("classId") Long classId, @Param("semester") String semester);
}
