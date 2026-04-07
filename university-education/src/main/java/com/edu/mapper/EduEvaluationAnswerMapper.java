package com.edu.mapper;

import com.edu.entity.EduEvaluationAnswer;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EduEvaluationAnswerMapper {
    List<EduEvaluationAnswer> selectByEvaluationId(@Param("evaluationId") Long evaluationId);
    int batchInsert(@Param("list") List<EduEvaluationAnswer> answers);
}
