package com.edu.mapper;

import com.edu.entity.EduTeachingMaterial;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduTeachingMaterialMapper {
    EduTeachingMaterial selectById(@Param("id") Long id);
    List<EduTeachingMaterial> selectByTeacherId(@Param("teacherId") Long teacherId);
    List<EduTeachingMaterial> selectPage(EduTeachingMaterial query);
    int insert(EduTeachingMaterial material);
    int update(EduTeachingMaterial material);
    int resetAudit(@Param("id") Long id, @Param("fileId") Long fileId);
    int deleteById(@Param("id") Long id);
}
