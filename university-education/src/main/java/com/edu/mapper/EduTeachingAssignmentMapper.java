package com.edu.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface EduTeachingAssignmentMapper {
    Map<String, Object> selectById(@Param("id") Long id);
    List<Map<String, Object>> selectList(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId, @Param("classId") Long classId, @Param("semester") String semester);
    List<Map<String, Object>> selectByTeacherId(@Param("teacherId") Long teacherId, @Param("semester") String semester);
    int insert(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId, @Param("classId") Long classId, @Param("semester") String semester);
    int deleteById(@Param("id") Long id);
    int deleteByIds(@Param("ids") List<Long> ids);
    int checkExists(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId, @Param("classId") Long classId);
}
