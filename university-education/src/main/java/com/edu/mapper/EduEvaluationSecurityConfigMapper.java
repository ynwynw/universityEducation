package com.edu.mapper;

import com.edu.entity.EduEvaluationSecurityConfig;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduEvaluationSecurityConfigMapper {

    EduEvaluationSecurityConfig selectById(@Param("id") Long id);

    List<EduEvaluationSecurityConfig> selectPage(EduEvaluationSecurityConfig query);

    EduEvaluationSecurityConfig selectBySemester(@Param("semester") String semester);

    int insert(EduEvaluationSecurityConfig config);

    int update(EduEvaluationSecurityConfig config);

    int deleteById(@Param("id") Long id);
}
