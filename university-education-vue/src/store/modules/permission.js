import { constantRoutes } from '@/router'

/**
 * 将后端返回的扁平菜单列表构建成树形结构
 */
function buildMenuTree(menus) {
  const map = {}
  const tree = []
  
  // 先建立 id -> menu 的映射
  menus.forEach(menu => {
    map[menu.id] = { ...menu, children: [] }
  })
  
  // 构建树
  menus.forEach(menu => {
    if (menu.parentId && menu.parentId !== 0 && map[menu.parentId]) {
      map[menu.parentId].children.push(map[menu.id])
    } else {
      tree.push(map[menu.id])
    }
  })
  
  return tree
}

/**
 * 根据树形菜单生成VueRouter路由
 */
function generateRoutesFromTree(menuTree) {
  const routes = []
  
  menuTree.forEach(menu => {
    // 跳过首页菜单
    if (menu.path === '/dashboard' || menu.component === 'dashboard/index') {
      return
    }
    
    if (menu.menuType === 'M' && menu.children && menu.children.length > 0) {
      // 目录类型：有子菜单
      const route = {
        path: menu.path.startsWith('/') ? menu.path : '/' + menu.path,
        component: () => import('@/layout/index.vue'),
        meta: { title: menu.menuName, icon: menu.icon },
        children: []
      }
      
      menu.children.forEach(child => {
        if (child.component) {
          route.children.push({
            path: child.path,
            name: child.path.replace(/[/-]/g, '_'),
            component: loadComponent(child.component),
            meta: { title: child.menuName, icon: child.icon },
            hidden: child.visible === 0
          })
        }
      })
      
      // 设置默认重定向到第一个可见子菜单
      const firstVisible = route.children.find(c => !c.hidden)
      if (firstVisible) {
        route.redirect = route.path + '/' + firstVisible.path
      }
      
      if (route.children.length > 0) {
        routes.push(route)
      }
    } else if (menu.menuType === 'C' && menu.component) {
      // 菜单类型：没有父级目录，直接挂在Layout下
      const route = {
        path: menu.path.startsWith('/') ? menu.path : '/' + menu.path,
        component: () => import('@/layout/index.vue'),
        children: [{
          path: '',
          name: menu.path.replace(/[/-]/g, '_'),
          component: loadComponent(menu.component),
          meta: { title: menu.menuName, icon: menu.icon }
        }]
      }
      routes.push(route)
    }
  })
  
  return routes
}

/**
 * 动态加载组件
 */
function loadComponent(component) {
  if (!component || component === 'Layout') {
    return () => import('@/layout/index.vue')
  }
  return () => import(`@/views/${component}.vue`)
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, menus) {
    return new Promise(resolve => {
      // 1. 构建树形结构
      const menuTree = buildMenuTree(menus)
      // 2. 生成路由
      const accessedRoutes = generateRoutesFromTree(menuTree)
      // 3. 添加404兜底
      accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
