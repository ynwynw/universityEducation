package com.edu.mapper;

import com.edu.entity.EduGradeWeightConfig;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduGradeWeightConfigMapper {
    List<EduGradeWeightConfig> selectAll();
    EduGradeWeightConfig selectByExamType(@Param("examType") String examType);
    int update(EduGradeWeightConfig config);
}
