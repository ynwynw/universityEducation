import request from '@/utils/request'

// 获取学院列表
export function getCollegePage(params) {
  return request({
    url: '/college/page',
    method: 'get',
    params
  })
}

// 获取所有学院
export function getCollegeList() {
  return request({
    url: '/college/list',
    method: 'get'
  })
}

// 获取学院详情
export function getCollege(id) {
  return request({
    url: `/college/${id}`,
    method: 'get'
  })
}

// 新增学院
export function addCollege(data) {
  return request({
    url: '/college',
    method: 'post',
    data
  })
}

// 修改学院
export function updateCollege(data) {
  return request({
    url: '/college',
    method: 'put',
    data
  })
}

// 删除学院
export function deleteCollege(id) {
  return request({
    url: `/college/${id}`,
    method: 'delete'
  })
}

// 批量删除学院
export function batchDeleteCollege(ids) {
  return request({
    url: '/college/batch',
    method: 'delete',
    data: ids
  })
}
