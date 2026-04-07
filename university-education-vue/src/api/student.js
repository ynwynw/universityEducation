import request from '@/utils/request'

export function getStudentPage(params) {
  return request({ url: '/student/page', method: 'get', params })
}

export function getStudentById(id) {
  return request({ url: `/student/${id}`, method: 'get' })
}

export function addStudent(data) {
  return request({ url: '/student', method: 'post', data })
}

export function updateStudent(data) {
  return request({ url: '/student', method: 'put', data })
}

export function deleteStudent(id) {
  return request({ url: `/student/${id}`, method: 'delete' })
}

export function batchDeleteStudent(ids) {
  return request({ url: '/student/batch', method: 'delete', data: ids })
}

export function getMyInfo() {
  return request({ url: '/student/info', method: 'get' })
}

export function updateMyInfo(data) {
  return request({ url: '/student/info', method: 'put', data })
}

// 下载学生导入模板
export function downloadStudentTemplate() {
  return request({ url: '/student/import/template', method: 'get', responseType: 'blob' })
}

// 导入学生
export function importStudents(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: '/student/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}
