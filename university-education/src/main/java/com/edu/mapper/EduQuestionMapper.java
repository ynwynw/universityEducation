package com.edu.mapper;

import com.edu.entity.EduQuestion;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduQuestionMapper {
    List<EduQuestion> selectByQuestionnaireId(@Param("questionnaireId") Long questionnaireId);
    EduQuestion selectById(@Param("id") Long id);
    int insert(EduQuestion question);
    int update(EduQuestion question);
    int deleteById(@Param("id") Long id);
    int deleteByQuestionnaireId(@Param("questionnaireId") Long questionnaireId);
}
