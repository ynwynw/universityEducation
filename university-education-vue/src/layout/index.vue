<template>
  <div class="app-wrapper" :class="{ 'sidebar-collapsed': !sidebarOpened }">
    <sidebar class="sidebar-container" />
    <div class="main-container">
      <navbar />
      <tags-view />
      <app-main />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Sidebar from './components/Sidebar.vue'
import Navbar from './components/Navbar.vue'
import TagsView from './components/TagsView.vue'
import AppMain from './components/AppMain.vue'

export default {
  name: 'Layout',
  components: {
    Sidebar,
    Navbar,
    TagsView,
    AppMain
  },
  computed: {
    ...mapGetters(['sidebarOpened'])
  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  
  .sidebar-container {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    width: $sideBarWidth;
    background-color: $sideBarBg;
    transition: width 0.3s;
    z-index: 1001;
    overflow: hidden;
  }
  
  .main-container {
    margin-left: $sideBarWidth;
    min-height: 100%;
    transition: margin-left 0.3s;
  }
  
  &.sidebar-collapsed {
    .sidebar-container {
      width: $sideBarCollapsedWidth;
    }
    .main-container {
      margin-left: $sideBarCollapsedWidth;
    }
  }
}
</style>
