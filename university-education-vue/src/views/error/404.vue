<template>
  <div class="error-page">
    <div class="error-content">
      <h1>404</h1>
      <p>抱歉，您访问的页面不存在</p>
      <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Error404'
}
</script>

<style lang="scss" scoped>
.error-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f0f2f5;
  
  .error-content {
    text-align: center;
    
    h1 {
      font-size: 120px;
      color: #409EFF;
      margin: 0;
    }
    
    p {
      font-size: 18px;
      color: #666;
      margin: 20px 0 30px;
    }
  }
}
</style>
