<template>
  <el-form
    ref="searchForm"
    :model="form"
    :inline="true"
    class="search-form"
    @submit.native.prevent
  >
    <slot></slot>
    <el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: 'SearchBar',
  props: {
    form: {
      type: Object,
      required: true
    }
  },
  methods: {
    handleSearch() {
      this.$emit('search')
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.$emit('reset')
    }
  }
}
</script>

<style lang="scss" scoped>
.search-form {
  padding: 18px 20px 0;
  background: #fff;
  margin-bottom: 20px;
  border-radius: 0;
  
  ::v-deep .el-form-item {
    margin-bottom: 18px;
  }
}
</style>
