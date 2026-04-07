import request from '@/utils/request'

export function getDashboardStats() {
  return request({ url: '/dashboard/stats', method: 'get' })
}

export function getLatestAnnouncements() {
  return request({ url: '/dashboard/announcements', method: 'get' })
}
