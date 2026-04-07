import request from '@/utils/request'

export function getMyMaterials() {
  return request({ url: '/teaching-material/my', method: 'get' })
}

export function getMaterialPage(params) {
  return request({ url: '/teaching-material/page', method: 'get', params })
}

export function uploadMaterial(data) {
  return request({
    url: '/teaching-material',
    method: 'post',
    data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function auditMaterial(id, auditStatus, auditRemark) {
  return request({
    url: `/teaching-material/audit/${id}`,
    method: 'put',
    params: { auditStatus, auditRemark }
  })
}

export function reuploadMaterial(id, data) {
  return request({
    url: `/teaching-material/reupload/${id}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
