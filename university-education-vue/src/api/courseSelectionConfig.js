import request from '@/utils/request'

export function getConfigPage(params) {
  return request({ url: '/course-selection-config/page', method: 'get', params })
}

export function getSelectionConfigs() {
  return request({ url: '/course-selection-config/list', method: 'get' })
}

export function addConfig(data) {
  return request({ url: '/course-selection-config', method: 'post', data })
}

export function updateConfig(id, data) {
  return request({ url: `/course-selection-config/${id}`, method: 'put', data })
}

export function deleteConfig(id) {
  return request({ url: `/course-selection-config/${id}`, method: 'delete' })
}

export function updateConfigStatus(id, status) {
  return request({ url: `/course-selection-config/status/${id}`, method: 'put', params: { status } })
}
