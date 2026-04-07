<template>
  <div class="sidebar">
    <div class="logo">
      <span v-if="sidebarOpened">教务管理系统</span>
      <span v-else>教务</span>
    </div>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="!sidebarOpened"
        :background-color="'#304156'"
        :text-color="'#bfcbd9'"
        :active-text-color="'#409EFF'"
        :unique-opened="true"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="route in routes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem.vue'

export default {
  name: 'Sidebar',
  components: { SidebarItem },
  computed: {
    ...mapGetters(['routes', 'sidebarOpened']),
    activeMenu() {
      const { path } = this.$route
      return path
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar {
  height: 100%;
  
  .logo {
    height: 50px;
    line-height: 50px;
    text-align: center;
    color: #fff;
    font-size: 18px;
    font-weight: bold;
    background-color: #263445;
    overflow: hidden;
  }
  
  .el-scrollbar {
    height: calc(100% - 50px);
  }
  
  .el-menu {
    border: none;
  }
}
</style>
