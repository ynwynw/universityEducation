import request from '@/utils/request'

export function getTeachingAssignmentPage(params) {
  return request({ url: '/teaching-assignment/page', method: 'get', params })
}

export function getMyAssignments(semester) {
  return request({ url: '/teaching-assignment/my', method: 'get', params: { semester } })
}

export function addTeachingAssignment(data) {
  return request({ url: '/teaching-assignment', method: 'post', data })
}

export function deleteTeachingAssignment(id) {
  return request({ url: `/teaching-assignment/${id}`, method: 'delete' })
}

export function batchDeleteTeachingAssignment(ids) {
  return request({ url: '/teaching-assignment/batch', method: 'delete', data: ids })
}
