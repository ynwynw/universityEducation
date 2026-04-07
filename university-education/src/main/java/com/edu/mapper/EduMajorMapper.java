package com.edu.mapper;

import com.edu.entity.EduMajor;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 专业 Mapper 接口
 */
public interface EduMajorMapper {
    
    EduMajor selectById(@Param("id") Long id);
    
    List<EduMajor> selectList(EduMajor major);
    
    List<EduMajor> selectByCollegeId(@Param("collegeId") Long collegeId);
    
    int insert(EduMajor major);
    
    int update(EduMajor major);
    
    int deleteById(@Param("id") Long id);
    
    int deleteByIds(@Param("ids") List<Long> ids);
    
    int checkCodeExists(@Param("majorCode") String majorCode, @Param("excludeId") Long excludeId);
    
    int countByCollegeId(@Param("collegeId") Long collegeId);
    
    EduMajor selectByMajorCode(@Param("majorCode") String majorCode);
}
