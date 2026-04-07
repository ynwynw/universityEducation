import request from '@/utils/request'

export function getEvaluationSecurityConfigPage(params) {
  return request({ url: '/evaluation-security-config/page', method: 'get', params })
}

export function addEvaluationSecurityConfig(data) {
  return request({ url: '/evaluation-security-config', method: 'post', data })
}

export function updateEvaluationSecurityConfig(id, data) {
  return request({ url: `/evaluation-security-config/${id}`, method: 'put', data })
}

export function deleteEvaluationSecurityConfig(id) {
  return request({ url: `/evaluation-security-config/${id}`, method: 'delete' })
}
