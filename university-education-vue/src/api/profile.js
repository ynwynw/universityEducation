import request from '@/utils/request'

// 获取当前用户个人信息
export function getMyProfile() {
  return request({
    url: '/profile/my',
    method: 'get'
  })
}

// 更新当前用户个人信息
export function updateMyProfile(data) {
  return request({
    url: '/profile/my',
    method: 'put',
    data
  })
}
