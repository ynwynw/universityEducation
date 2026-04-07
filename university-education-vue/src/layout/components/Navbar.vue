<template>
  <div class="navbar">
    <div class="left-menu">
      <i
        :class="sidebarOpened ? 'el-icon-s-fold' : 'el-icon-s-unfold'"
        class="hamburger"
        @click="toggleSidebar"
      ></i>
      <breadcrumb class="breadcrumb-container" />
    </div>
    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <span class="user-name">{{ userInfo.realName || userInfo.username }}</span>
          <i class="el-icon-caret-bottom"></i>
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="handleProfile">
            <i class="el-icon-user"></i> 个人中心
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="handleLogout">
            <i class="el-icon-switch-button"></i> 退出登录
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from './Breadcrumb.vue'

export default {
  name: 'Navbar',
  components: { Breadcrumb },
  computed: {
    ...mapGetters(['sidebarOpened', 'userInfo'])
  },
  methods: {
    toggleSidebar() {
      this.$store.dispatch('app/toggleSidebar')
    },
    handleProfile() {
      this.$router.push('/profile')
    },
    async handleLogout() {
      await this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await this.$store.dispatch('user/logout')
      this.$router.push('/login')
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: $headerHeight;
  background: $headerBg;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15px;
  
  .left-menu {
    display: flex;
    align-items: center;
    
    .hamburger {
      font-size: 20px;
      cursor: pointer;
      margin-right: 15px;
      
      &:hover {
        color: $--color-primary;
      }
    }
  }
  
  .right-menu {
    .avatar-wrapper {
      cursor: pointer;
      display: flex;
      align-items: center;
      
      .user-name {
        margin-right: 5px;
      }
    }
  }
}
</style>
