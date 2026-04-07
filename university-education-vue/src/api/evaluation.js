import request from '@/utils/request'

export function getPendingCourses() {
  return request({ url: '/evaluation/pending', method: 'get' })
}

export function submitEvaluation(data) {
  return request({ url: '/evaluation/submit', method: 'post', data })
}

export function getPendingTeachers() {
  return request({ url: '/evaluation/teacher/pending', method: 'get' })
}

export function submitTeacherEvaluation(data) {
  return request({ url: '/evaluation/teacher/submit', method: 'post', data })
}

export function getMyResult(semester) {
  return request({ url: '/evaluation/my-result', method: 'get', params: { semester } })
}

export function getEvaluationPage(params) {
  return request({ url: '/evaluation/page', method: 'get', params })
}

export function getEvaluationStatistics(params) {
  return request({ url: '/evaluation/statistics', method: 'get', params })
}
