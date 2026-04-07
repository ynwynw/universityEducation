package com.edu.mapper;

import com.edu.entity.EduCourseSelectionConfig;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduCourseSelectionConfigMapper {

    EduCourseSelectionConfig selectById(@Param("id") Long id);

    List<EduCourseSelectionConfig> selectPage(EduCourseSelectionConfig query);

    EduCourseSelectionConfig selectBySemester(@Param("semester") String semester);

    int insert(EduCourseSelectionConfig config);

    int update(EduCourseSelectionConfig config);

    int deleteById(@Param("id") Long id);
}
