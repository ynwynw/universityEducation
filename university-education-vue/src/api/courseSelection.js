import request from '@/utils/request'

export function getAvailableCourses(semester) {
  return request({ url: '/course-selection/available', method: 'get', params: { semester } })
}

export function getMyCourses(semester) {
  return request({ url: '/course-selection/my', method: 'get', params: { semester } })
}

export function selectCourse(teachingAssignmentId) {
  return request({ url: `/course-selection/select/${teachingAssignmentId}`, method: 'post' })
}

export function cancelCourse(teachingAssignmentId) {
  return request({ url: `/course-selection/cancel/${teachingAssignmentId}`, method: 'delete' })
}

export function getCourseSelectionPage(params) {
  return request({ url: '/course-selection/page', method: 'get', params })
}
