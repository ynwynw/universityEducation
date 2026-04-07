import request from '@/utils/request'

// 获取专业列表
export function getMajorPage(params) {
  return request({
    url: '/major/page',
    method: 'get',
    params
  })
}

// 根据学院获取专业列表
export function getMajorsByCollege(collegeId) {
  return request({
    url: '/major/list',
    method: 'get',
    params: { collegeId }
  })
}

// 别名导出，兼容旧代码
export const getMajorList = getMajorsByCollege

// 获取专业详情
export function getMajor(id) {
  return request({
    url: `/major/${id}`,
    method: 'get'
  })
}

// 新增专业
export function addMajor(data) {
  return request({
    url: '/major',
    method: 'post',
    data
  })
}

// 修改专业
export function updateMajor(data) {
  return request({
    url: '/major',
    method: 'put',
    data
  })
}

// 删除专业
export function deleteMajor(id) {
  return request({
    url: `/major/${id}`,
    method: 'delete'
  })
}

// 批量删除专业
export function batchDeleteMajor(ids) {
  return request({
    url: '/major/batch',
    method: 'delete',
    data: ids
  })
}
