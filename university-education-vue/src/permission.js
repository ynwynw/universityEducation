import router from './router'
import store from './store'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { getMenus } from '@/api/auth'

NProgress.configure({ showSpinner: false })

// 白名单
const whiteList = ['/login', '/404']

router.beforeEach(async (to, from, next) => {
  NProgress.start()
  
  const hasToken = getToken()
  
  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      // 检查是否已获取用户信息
      const hasUserInfo = store.getters.userInfo && Object.keys(store.getters.userInfo).length > 0
      if (hasUserInfo) {
        next()
      } else {
        try {
          // 获取用户信息
          const userData = await store.dispatch('user/getUserInfo')
          // 获取菜单并生成路由
          const { data } = await getMenus()
          const accessRoutes = await store.dispatch('permission/generateRoutes', data)
          // 动态添加路由
          accessRoutes.forEach(route => {
            router.addRoute(route)
          })
          next({ ...to, replace: true })
        } catch (error) {
          console.error('获取用户信息失败:', error)
          await store.dispatch('user/resetToken')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
