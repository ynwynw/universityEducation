import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 添加 token
    if (store.getters.token) {
      config.headers['satoken'] = getToken()
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 文件下载请求，直接返回 response
    if (response.config.responseType === 'blob') {
      return response
    }

    const res = response.data
    
    // 成功
    if (res.code === 200) {
      return res
    }
    
    // 未登录或token过期
    if (res.code === 401) {
      MessageBox.confirm('登录已过期，请重新登录', '提示', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        store.dispatch('user/logout').then(() => {
          location.reload()
        })
      })
      return Promise.reject(new Error(res.msg || '未登录'))
    }
    
    // 其他错误
    Message({
      message: res.msg || '请求失败',
      type: 'error',
      duration: 3000
    })
    return Promise.reject(new Error(res.msg || '请求失败'))
  },
  error => {
    console.error('响应错误:', error)
    Message({
      message: error.message || '网络错误',
      type: 'error',
      duration: 3000
    })
    return Promise.reject(error)
  }
)

export default service
