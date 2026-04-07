package com.edu.mapper;

import com.edu.entity.EduCollege;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 学院 Mapper 接口
 */
public interface EduCollegeMapper {
    
    EduCollege selectById(@Param("id") Long id);
    
    List<EduCollege> selectList(EduCollege college);
    
    List<EduCollege> selectAll();
    
    int insert(EduCollege college);
    
    int update(EduCollege college);
    
    int deleteById(@Param("id") Long id);
    
    int deleteByIds(@Param("ids") List<Long> ids);
    
    int checkCodeExists(@Param("collegeCode") String collegeCode, @Param("excludeId") Long excludeId);
    
    int checkNameExists(@Param("collegeName") String collegeName, @Param("excludeId") Long excludeId);
    
    int count();
}
