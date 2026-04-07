<template>
  <div class="tags-view-container">
    <div class="tags-view-wrapper" ref="scrollContainer">
      <router-link
        v-for="tag in visitedViews"
        :key="tag.path"
        :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
        :class="isActive(tag) ? 'active' : ''"
        class="tags-view-item"
        @contextmenu.prevent.native="openMenu(tag, $event)"
      >
        {{ tag.title }}
        <span
          v-if="!isAffix(tag)"
          class="el-icon-close"
          @click.prevent.stop="closeTag(tag)"
        />
      </router-link>
    </div>
    <ul v-show="menuVisible" :style="{ left: menuLeft + 'px', top: menuTop + 'px' }" class="contextmenu">
      <li @click="closeTag(selectedTag)">关闭</li>
      <li @click="closeOthersTags">关闭其他</li>
      <li @click="closeAllTags">关闭所有</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'TagsView',
  data() {
    return {
      menuVisible: false,
      menuLeft: 0,
      menuTop: 0,
      selectedTag: {},
      affixTags: []
    }
  },
  computed: {
    visitedViews() {
      return this.$store.state.tagsView.visitedViews
    }
  },
  watch: {
    $route() {
      this.addTag()
    },
    menuVisible(val) {
      if (val) {
        document.body.addEventListener('click', this.closeMenu)
      } else {
        document.body.removeEventListener('click', this.closeMenu)
      }
    }
  },
  mounted() {
    this.initTags()
    this.addTag()
  },
  methods: {
    isActive(tag) {
      return tag.path === this.$route.path
    },
    isAffix(tag) {
      return tag.meta && tag.meta.affix
    },
    initTags() {
      // 添加首页标签
      const routes = this.$store.state.permission.routes
      for (const route of routes) {
        if (route.children) {
          for (const child of route.children) {
            if (child.meta && child.meta.affix) {
              const path = route.path === '/' ? '/' + child.path : route.path + '/' + child.path
              this.$store.dispatch('tagsView/addView', {
                name: child.name,
                path: path,
                fullPath: path,
                meta: child.meta
              })
            }
          }
        }
      }
    },
    addTag() {
      const { name, path, fullPath, meta, query } = this.$route
      if (name && meta && meta.title) {
        this.$store.dispatch('tagsView/addView', { name, path, fullPath, meta, query })
      }
    },
    closeTag(tag) {
      if (this.isAffix(tag)) return
      this.$store.dispatch('tagsView/delView', tag).then(views => {
        if (this.isActive(tag)) {
          this.toLastView(views)
        }
      })
    },
    closeOthersTags() {
      this.$router.push(this.selectedTag)
      this.$store.dispatch('tagsView/delOthersViews', this.selectedTag)
    },
    closeAllTags() {
      this.$store.dispatch('tagsView/delAllViews').then(views => {
        this.toLastView(views)
      })
    },
    toLastView(views) {
      const latestView = views.slice(-1)[0]
      if (latestView) {
        this.$router.push(latestView.fullPath)
      } else {
        this.$router.push('/')
      }
    },
    openMenu(tag, e) {
      this.menuLeft = e.clientX
      this.menuTop = e.clientY
      this.menuVisible = true
      this.selectedTag = tag
    },
    closeMenu() {
      this.menuVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.tags-view-container {
  height: $tagsViewHeight;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);
  position: relative;

  .tags-view-wrapper {
    height: 100%;
    display: flex;
    align-items: center;
    padding: 0 10px;
    overflow-x: auto;
    white-space: nowrap;

    &::-webkit-scrollbar {
      height: 0;
    }

    .tags-view-item {
      display: inline-flex;
      align-items: center;
      height: 26px;
      line-height: 26px;
      border: 1px solid #d8dce5;
      color: #495060;
      background: #fff;
      padding: 0 8px;
      font-size: 12px;
      margin-left: 5px;
      text-decoration: none;
      cursor: pointer;
      flex-shrink: 0;

      &:first-of-type {
        margin-left: 10px;
      }

      &:last-of-type {
        margin-right: 10px;
      }

      &.active {
        background-color: #409EFF;
        color: #fff;
        border-color: #409EFF;

        .el-icon-close {
          color: #fff;
        }
      }

      .el-icon-close {
        font-size: 12px;
        margin-left: 4px;
        vertical-align: middle;

        &:hover {
          background-color: rgba(0, 0, 0, 0.16);
          color: #fff;
        }
      }
    }
  }

  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: fixed;
    list-style-type: none;
    padding: 5px 0;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);

    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;

      &:hover {
        background: #eee;
      }
    }
  }
}
</style>
