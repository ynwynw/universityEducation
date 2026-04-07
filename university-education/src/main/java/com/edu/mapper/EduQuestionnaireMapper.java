package com.edu.mapper;

import com.edu.entity.EduQuestionnaire;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduQuestionnaireMapper {
    EduQuestionnaire selectById(@Param("id") Long id);
    List<EduQuestionnaire> selectPage(EduQuestionnaire query);
    EduQuestionnaire selectActiveByType(@Param("questionnaireType") String questionnaireType);
    int insert(EduQuestionnaire questionnaire);
    int update(EduQuestionnaire questionnaire);
    int deleteById(@Param("id") Long id);
    int countEvaluationsByQuestionnaireId(@Param("questionnaireId") Long questionnaireId);
}
