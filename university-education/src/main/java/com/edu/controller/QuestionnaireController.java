package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduQuestion;
import com.edu.entity.EduQuestionnaire;
import com.edu.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @GetMapping("/page")
    @SaCheckPermission("system:questionnaire:list")
    public Result<PageResult<EduQuestionnaire>> getPage(
            EduQuestionnaire query,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(questionnaireService.getPage(query, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<EduQuestionnaire> getById(@PathVariable Long id) {
        return Result.success(questionnaireService.getById(id));
    }

    @GetMapping("/active")
    public Result<EduQuestionnaire> getActive(@RequestParam String questionnaireType) {
        return Result.success(questionnaireService.getActiveByType(questionnaireType));
    }

    @PostMapping
    @SaCheckPermission("system:questionnaire:add")
    @Log(module = "问卷管理", operation = "创建问卷")
    public Result<Void> create(@RequestBody EduQuestionnaire questionnaire) {
        questionnaireService.create(questionnaire);
        return Result.success();
    }

    @PutMapping("/{id}")
    @SaCheckPermission("system:questionnaire:edit")
    @Log(module = "问卷管理", operation = "更新问卷")
    public Result<Void> update(@PathVariable Long id, @RequestBody EduQuestionnaire questionnaire) {
        questionnaire.setId(id);
        questionnaireService.update(questionnaire);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:questionnaire:delete")
    @Log(module = "问卷管理", operation = "删除问卷")
    public Result<Void> delete(@PathVariable Long id) {
        questionnaireService.delete(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    @SaCheckPermission("system:questionnaire:edit")
    @Log(module = "问卷管理", operation = "更新问卷状态")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        questionnaireService.updateStatus(id, status);
        return Result.success();
    }

    // ========== 问题管理 ==========

    @GetMapping("/{id}/questions")
    public Result<List<EduQuestion>> getQuestions(@PathVariable Long id) {
        return Result.success(questionnaireService.getQuestions(id));
    }

    @PostMapping("/{id}/question")
    @SaCheckPermission("system:questionnaire:edit")
    @Log(module = "问卷管理", operation = "添加问题")
    public Result<Void> addQuestion(@PathVariable Long id, @RequestBody EduQuestion question) {
        question.setQuestionnaireId(id);
        questionnaireService.addQuestion(question);
        return Result.success();
    }

    @PutMapping("/question/{questionId}")
    @SaCheckPermission("system:questionnaire:edit")
    @Log(module = "问卷管理", operation = "更新问题")
    public Result<Void> updateQuestion(@PathVariable Long questionId, @RequestBody EduQuestion question) {
        question.setId(questionId);
        questionnaireService.updateQuestion(question);
        return Result.success();
    }

    @DeleteMapping("/question/{questionId}")
    @SaCheckPermission("system:questionnaire:edit")
    @Log(module = "问卷管理", operation = "删除问题")
    public Result<Void> deleteQuestion(@PathVariable Long questionId) {
        questionnaireService.deleteQuestion(questionId);
        return Result.success();
    }
}
