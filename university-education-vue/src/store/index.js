import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import permission from './modules/permission'
import app from './modules/app'
import tagsView from './modules/tagsView'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
    permission,
    app,
    tagsView
  },
  getters: {
    token: state => state.user.token,
    userInfo: state => state.user.userInfo,
    roles: state => state.user.roles,
    permissions: state => state.user.permissions,
    routes: state => state.permission.routes,
    sidebarOpened: state => state.app.sidebarOpened,
    visitedViews: state => state.tagsView.visitedViews
  }
})

export default store
