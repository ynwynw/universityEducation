import request from '@/utils/request'

export function getAnnouncementPage(params) {
  return request({ url: '/announcement/page', method: 'get', params })
}

export function getAnnouncementById(id) {
  return request({ url: `/announcement/${id}`, method: 'get' })
}

export function addAnnouncement(data) {
  return request({ url: '/announcement', method: 'post', data })
}

export function updateAnnouncement(data) {
  return request({ url: '/announcement', method: 'put', data })
}

export function deleteAnnouncement(id) {
  return request({ url: `/announcement/${id}`, method: 'delete' })
}

export function batchDeleteAnnouncement(ids) {
  return request({ url: '/announcement/batch', method: 'delete', data: ids })
}

export function getMyAnnouncements() {
  return request({ url: '/announcement/list', method: 'get' })
}

export function getUnreadCount() {
  return request({ url: '/announcement/unread/count', method: 'get' })
}
