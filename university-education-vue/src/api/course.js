import request from '@/utils/request'

export function getCoursePage(params) {
  return request({ url: '/course/page', method: 'get', params })
}

export function getCourseById(id) {
  return request({ url: `/course/${id}`, method: 'get' })
}

export function addCourse(data) {
  return request({ url: '/course', method: 'post', data })
}

export function updateCourse(data) {
  return request({ url: '/course', method: 'put', data })
}

export function deleteCourse(id) {
  return request({ url: `/course/${id}`, method: 'delete' })
}

export function batchDeleteCourse(ids) {
  return request({ url: '/course/batch', method: 'delete', data: ids })
}

export function getElectiveCourses(semester) {
  return request({ url: '/course/elective', method: 'get', params: { semester } })
}
