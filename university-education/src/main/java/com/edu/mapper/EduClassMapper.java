package com.edu.mapper;

import com.edu.entity.EduClass;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 班级Mapper接口
 */
public interface EduClassMapper {
    
    EduClass selectById(@Param("id") Long id);
    
    List<EduClass> selectList(EduClass eduClass);
    
    List<EduClass> selectByMajorId(@Param("majorId") Long majorId);
    
    int insert(EduClass eduClass);
    
    int update(EduClass eduClass);
    
    int deleteById(@Param("id") Long id);
    
    int deleteByIds(@Param("ids") List<Long> ids);
    
    int checkNameExists(@Param("className") String className, @Param("excludeId") Long excludeId);
    
    int countByMajorId(@Param("majorId") Long majorId);
    
    int updateStudentCount(@Param("id") Long id, @Param("count") Integer count);
    
    EduClass selectByClassName(@Param("className") String className);
}
