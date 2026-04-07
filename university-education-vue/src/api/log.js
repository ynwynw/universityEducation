import request from '@/utils/request'

export function getLogPage(params) {
  return request({ url: '/log/page', method: 'get', params })
}
