package com.edu.service;

import com.edu.common.PageResult;
import com.edu.entity.EduQuestion;
import com.edu.entity.EduQuestionnaire;
import com.edu.exception.BusinessException;
import com.edu.mapper.EduQuestionMapper;
import com.edu.mapper.EduQuestionnaireMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionnaireService {

    @Autowired
    private EduQuestionnaireMapper questionnaireMapper;

    @Autowired
    private EduQuestionMapper questionMapper;

    public PageResult<EduQuestionnaire> getPage(EduQuestionnaire query, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduQuestionnaire> list = questionnaireMapper.selectPage(query);
        PageInfo<EduQuestionnaire> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), list);
    }

    public EduQuestionnaire getById(Long id) {
        EduQuestionnaire q = questionnaireMapper.selectById(id);
        if (q == null) {
            throw BusinessException.notFound("问卷");
        }
        q.setQuestions(questionMapper.selectByQuestionnaireId(id));
        return q;
    }

    public EduQuestionnaire getActiveByType(String type) {
        return questionnaireMapper.selectActiveByType(type);
    }

    @Transactional
    public void create(EduQuestionnaire questionnaire) {
        if (questionnaire.getStatus() == null) {
            questionnaire.setStatus(0);
        }
        questionnaireMapper.insert(questionnaire);
    }

    @Transactional
    public void update(EduQuestionnaire questionnaire) {
        checkNotUsed(questionnaire.getId());
        questionnaireMapper.update(questionnaire);
    }

    @Transactional
    public void delete(Long id) {
        int count = questionnaireMapper.countEvaluationsByQuestionnaireId(id);
        if (count > 0) {
            throw new BusinessException("该问卷已有评教记录，不能删除");
        }
        questionMapper.deleteByQuestionnaireId(id);
        questionnaireMapper.deleteById(id);
    }

    @Transactional
    public void updateStatus(Long id, Integer status) {
        EduQuestionnaire update = new EduQuestionnaire();
        update.setId(id);
        update.setStatus(status);
        questionnaireMapper.update(update);
    }

    // ========== 问题管理 ==========

    public List<EduQuestion> getQuestions(Long questionnaireId) {
        return questionMapper.selectByQuestionnaireId(questionnaireId);
    }

    @Transactional
    public void addQuestion(EduQuestion question) {
        checkNotUsed(question.getQuestionnaireId());
        questionMapper.insert(question);
    }

    @Transactional
    public void updateQuestion(EduQuestion question) {
        EduQuestion existing = questionMapper.selectById(question.getId());
        if (existing == null) {
            throw BusinessException.notFound("问题");
        }
        checkNotUsed(existing.getQuestionnaireId());
        questionMapper.update(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        EduQuestion existing = questionMapper.selectById(questionId);
        if (existing == null) {
            throw BusinessException.notFound("问题");
        }
        checkNotUsed(existing.getQuestionnaireId());
        questionMapper.deleteById(questionId);
    }

    private void checkNotUsed(Long questionnaireId) {
        int count = questionnaireMapper.countEvaluationsByQuestionnaireId(questionnaireId);
        if (count > 0) {
            throw new BusinessException("该问卷已有评教记录，不能修改问题");
        }
    }
}
