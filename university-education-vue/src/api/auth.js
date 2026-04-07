import request from '@/utils/request'

// 获取验证码
export function getCaptcha() {
  return request({
    url: '/auth/captcha',
    method: 'get'
  })
}

// 登录
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 登出
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/auth/info',
    method: 'get'
  })
}

// 获取用户菜单
export function getMenus() {
  return request({
    url: '/auth/menus',
    method: 'get'
  })
}

// 修改密码
export function changePassword(data) {
  return request({
    url: '/auth/change-password',
    method: 'post',
    data
  })
}
