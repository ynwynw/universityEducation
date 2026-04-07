import request from '@/utils/request'

export function getGradePage(params) {
  return request({ url: '/grade/page', method: 'get', params })
}

export function getMyGrades(semester) {
  return request({ url: '/grade/my', method: 'get', params: { semester } })
}

export function getMySemesters() {
  return request({ url: '/grade/my/semesters', method: 'get' })
}

export function getStudentGrades(teachingAssignmentId) {
  return request({ url: `/grade/teaching/${teachingAssignmentId}`, method: 'get' })
}

export function saveGrade(data, midtermWeight, finalWeight, practiceWeight) {
  return request({ 
    url: '/grade', 
    method: 'post', 
    data,
    params: { midtermWeight, finalWeight, practiceWeight }
  })
}

export function saveMakeupScore(gradeId, makeupScore) {
  return request({ url: '/grade/makeup', method: 'post', params: { gradeId, makeupScore } })
}

export function getGradeStatistics(params) {
  return request({ url: '/grade/statistics', method: 'get', params })
}

export function getScoreDistribution(params) {
  return request({ url: '/grade/distribution', method: 'get', params })
}

export function batchSaveGrades(data, midtermWeight, finalWeight, practiceWeight) {
  return request({
    url: '/grade/batch',
    method: 'post',
    data,
    params: { midtermWeight, finalWeight, practiceWeight }
  })
}

export function teacherBatchSaveGrades(data, midtermWeight, finalWeight, practiceWeight) {
  return request({
    url: '/grade/teacher/batch',
    method: 'post',
    data,
    params: { midtermWeight, finalWeight, practiceWeight }
  })
}

export function exportGrades(teachingAssignmentId) {
  return request({ url: `/grade/export/${teachingAssignmentId}`, method: 'get', responseType: 'blob' })
}
