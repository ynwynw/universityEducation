package com.edu.mapper;

import com.edu.entity.EduTeacher;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduTeacherMapper {
    EduTeacher selectById(@Param("id") Long id);
    EduTeacher selectByTeacherNo(@Param("teacherNo") String teacherNo);
    List<EduTeacher> selectList(EduTeacher teacher);
    List<EduTeacher> selectByCollegeId(@Param("collegeId") Long collegeId);
    int insert(EduTeacher teacher);
    int update(EduTeacher teacher);
    int deleteById(@Param("id") Long id);
    int deleteByIds(@Param("ids") List<Long> ids);
    int checkTeacherNoExists(@Param("teacherNo") String teacherNo, @Param("excludeId") Long excludeId);
    int count();
}
