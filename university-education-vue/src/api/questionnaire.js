import request from '@/utils/request'

export function getQuestionnairePage(params) {
  return request({ url: '/questionnaire/page', method: 'get', params })
}

export function getQuestionnaireById(id) {
  return request({ url: `/questionnaire/${id}`, method: 'get' })
}

export function getActiveQuestionnaire(questionnaireType) {
  return request({ url: '/questionnaire/active', method: 'get', params: { questionnaireType } })
}

export function addQuestionnaire(data) {
  return request({ url: '/questionnaire', method: 'post', data })
}

export function updateQuestionnaire(id, data) {
  return request({ url: `/questionnaire/${id}`, method: 'put', data })
}

export function deleteQuestionnaire(id) {
  return request({ url: `/questionnaire/${id}`, method: 'delete' })
}

export function updateQuestionnaireStatus(id, status) {
  return request({ url: `/questionnaire/status/${id}`, method: 'put', params: { status } })
}

export function getQuestions(questionnaireId) {
  return request({ url: `/questionnaire/${questionnaireId}/questions`, method: 'get' })
}

export function addQuestion(questionnaireId, data) {
  return request({ url: `/questionnaire/${questionnaireId}/question`, method: 'post', data })
}

export function updateQuestion(questionId, data) {
  return request({ url: `/questionnaire/question/${questionId}`, method: 'put', data })
}

export function deleteQuestion(questionId) {
  return request({ url: `/questionnaire/question/${questionId}`, method: 'delete' })
}
