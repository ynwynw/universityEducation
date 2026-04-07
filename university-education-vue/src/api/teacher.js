import request from '@/utils/request'

export function getTeacherPage(params) {
  return request({ url: '/teacher/page', method: 'get', params })
}

export function getTeacherById(id) {
  return request({ url: `/teacher/${id}`, method: 'get' })
}

export function addTeacher(data) {
  return request({ url: '/teacher', method: 'post', data })
}

export function updateTeacher(data) {
  return request({ url: '/teacher', method: 'put', data })
}

export function deleteTeacher(id) {
  return request({ url: `/teacher/${id}`, method: 'delete' })
}

export function batchDeleteTeacher(ids) {
  return request({ url: '/teacher/batch', method: 'delete', data: ids })
}

export function getTeacherList(collegeId) {
  return request({ url: `/teacher/college/${collegeId}`, method: 'get' })
}
