import request from '@/utils/request'

// 获取班级列表
export function getClassPage(params) {
  return request({
    url: '/class/page',
    method: 'get',
    params
  })
}

// 根据专业获取班级列表
export function getClassesByMajor(majorId) {
  return request({
    url: '/class/list',
    method: 'get',
    params: { majorId }
  })
}

// 别名导出，兼容旧代码
export const getClassList = getClassesByMajor

// 获取班级详情
export function getClass(id) {
  return request({
    url: `/class/${id}`,
    method: 'get'
  })
}

// 新增班级
export function addClass(data) {
  return request({
    url: '/class',
    method: 'post',
    data
  })
}

// 修改班级
export function updateClass(data) {
  return request({
    url: '/class',
    method: 'put',
    data
  })
}

// 删除班级
export function deleteClass(id) {
  return request({
    url: `/class/${id}`,
    method: 'delete'
  })
}

// 批量删除班级
export function batchDeleteClass(ids) {
  return request({
    url: '/class/batch',
    method: 'delete',
    data: ids
  })
}

// 获取班级学生列表
export function getClassStudents(classId) {
  return request({
    url: `/class/${classId}/students`,
    method: 'get'
  })
}

// 导出班级学生名单
export function exportClassStudents(classId) {
  return request({
    url: `/class/${classId}/export`,
    method: 'get',
    responseType: 'blob',
    // 标记为下载请求，拦截器需要特殊处理
    isDownload: true
  })
}

// 下载学生导入模板
export function downloadImportTemplate() {
  return request({
    url: '/class/import/template',
    method: 'get',
    responseType: 'blob'
  })
}

// 导入学生
export function importClassStudents(classId, file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: `/class/${classId}/import`,
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
