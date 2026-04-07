<template>
  <div v-if="!item.hidden">
    <!-- 只有一个子菜单或没有子菜单 -->
    <template v-if="hasOneShowingChild(item.children, item)">
      <el-menu-item
        v-if="onlyOneChild.meta"
        :index="resolvePath(onlyOneChild.path)"
        @click="handleClick"
      >
        <i :class="onlyOneChild.meta.icon || item.meta.icon || 'el-icon-menu'"></i>
        <span slot="title">{{ onlyOneChild.meta.title }}</span>
      </el-menu-item>
    </template>
    
    <!-- 有多个子菜单 -->
    <el-submenu v-else :index="resolvePath(item.path)">
      <template slot="title">
        <i :class="item.meta && item.meta.icon || 'el-icon-menu'"></i>
        <span>{{ item.meta && item.meta.title }}</span>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :item="child"
        :base-path="resolvePath(child.path)"
      />
    </el-submenu>
  </div>
</template>

<script>
export default {
  name: 'SidebarItem',
  props: {
    item: {
      type: Object,
      required: true
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      onlyOneChild: null
    }
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          this.onlyOneChild = item
          return true
        }
      })
      
      if (showingChildren.length === 1) {
        return true
      }
      
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ...parent, path: '', noShowingChildren: true }
        return true
      }
      
      return false
    },
    resolvePath(routePath) {
      if (routePath.startsWith('/')) {
        return routePath
      }
      // 去掉basePath末尾的斜杠
      const base = this.basePath.endsWith('/') ? this.basePath.slice(0, -1) : this.basePath
      if (base) {
        return base + '/' + routePath
      }
      return '/' + routePath
    },
    handleClick() {
      const path = this.resolvePath(this.onlyOneChild.path)
      if (this.$route.path !== path) {
        this.$router.push(path)
      }
    }
  }
}
</script>
