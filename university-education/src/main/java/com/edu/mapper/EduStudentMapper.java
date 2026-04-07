package com.edu.mapper;

import com.edu.entity.EduStudent;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduStudentMapper {
    EduStudent selectById(@Param("id") Long id);
    EduStudent selectByStudentNo(@Param("studentNo") String studentNo);
    List<EduStudent> selectList(EduStudent student);
    List<EduStudent> selectByClassId(@Param("classId") Long classId);
    int insert(EduStudent student);
    int update(EduStudent student);
    int deleteById(@Param("id") Long id);
    int deleteByIds(@Param("ids") List<Long> ids);
    int checkStudentNoExists(@Param("studentNo") String studentNo, @Param("excludeId") Long excludeId);
    int countByClassId(@Param("classId") Long classId);
    int count();
}
