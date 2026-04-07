package com.edu.mapper;

import com.edu.entity.EduCourseCollegeLimit;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduCourseCollegeLimitMapper {

    List<EduCourseCollegeLimit> selectPage(EduCourseCollegeLimit query);

    List<EduCourseCollegeLimit> selectByCourseCode(@Param("courseCode") String courseCode);

    int checkExists(@Param("courseCode") String courseCode, @Param("collegeId") Long collegeId);

    int insert(EduCourseCollegeLimit limit);

    int deleteById(@Param("id") Long id);
}
