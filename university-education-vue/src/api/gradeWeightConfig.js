import request from '@/utils/request'

export function getConfigList() {
  return request({ url: '/grade-weight-config/list', method: 'get' })
}

export function updateConfig(id, data) {
  return request({ url: `/grade-weight-config/${id}`, method: 'put', data })
}
