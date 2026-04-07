package com.edu.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface EduEvaluationMapper {
    List<Map<String, Object>> selectPendingCourses(@Param("studentId") Long studentId);
    int checkStudentEvaluated(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("teacherId") Long teacherId);
    int insertStudentEvaluation(@Param("questionnaireId") Long questionnaireId, @Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("teacherId") Long teacherId,
                                 @Param("totalScore") Double totalScore, @Param("gradeLevel") String gradeLevel, @Param("comment") Object comment, @Param("semester") String semester);
    List<Map<String, Object>> selectPendingTeachers(@Param("teacherId") Long teacherId);
    int checkTeacherEvaluated(@Param("evaluatorId") Long evaluatorId, @Param("targetTeacherId") Long targetTeacherId);
    int insertTeacherEvaluation(@Param("questionnaireId") Long questionnaireId, @Param("evaluatorId") Long evaluatorId, @Param("targetTeacherId") Long targetTeacherId,
                                 @Param("totalScore") Double totalScore, @Param("gradeLevel") String gradeLevel, @Param("comment") Object comment, @Param("semester") String semester);
    Map<String, Object> selectTeacherResult(@Param("teacherId") Long teacherId, @Param("semester") String semester);
    List<Map<String, Object>> selectGradeDistribution(@Param("teacherId") Long teacherId, @Param("semester") String semester);
    List<Map<String, Object>> selectPage(@Param("teacherId") Long teacherId, @Param("semester") String semester);
    Map<String, Object> selectStatistics(@Param("collegeId") Long collegeId, @Param("semester") String semester);
    Long selectLastInsertId();
    List<Double> selectScoresByTeacher(@Param("teacherId") Long teacherId, @Param("semester") String semester);
}
