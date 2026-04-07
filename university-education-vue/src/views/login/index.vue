<template>
  <div class="login-page">
    <!-- 中间主体区域：左图右表单 -->
    <div class="login-body">
      <div class="login-left">
        <img src="@/icon/image.png" alt="背景图" class="bg-image" />
      </div>
      <div class="login-right">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
          <h2 class="system-name">教务管理系统</h2>
          <h3 class="form-title"><i class="el-icon-setting"></i> 系统登录</h3>
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              prefix-icon="el-icon-user"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
              show-password
              @keyup.enter.native="handleLogin"
            />
          </el-form-item>
          <el-form-item prop="captcha">
            <el-row :gutter="10">
              <el-col :span="16">
                <el-input
                  v-model="loginForm.captcha"
                  placeholder="请输入验证码"
                  prefix-icon="el-icon-key"
                  @keyup.enter.native="handleLogin"
                />
              </el-col>
              <el-col :span="8">
                <img
                  :src="captchaUrl"
                  class="captcha-img"
                  @click="refreshCaptcha"
                  alt="验证码"
                />
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item>
            <el-button
              :loading="loading"
              type="primary"
              style="width: 100%"
              @click="handleLogin"
            >
              登 录
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { getCaptcha } from '@/api/auth'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        captcha: '',
        captchaKey: ''
      },
      loginRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      },
      loading: false,
      captchaUrl: ''
    }
  },
  created() {
    this.refreshCaptcha()
  },
  methods: {
    async refreshCaptcha() {
      const { data } = await getCaptcha()
      this.captchaUrl = data.image
      this.loginForm.captchaKey = data.key
    },
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (valid) {
          this.loading = true
          try {
            await this.$store.dispatch('user/login', this.loginForm)
            const redirect = this.$route.query.redirect || '/'
            this.$router.push(redirect)
          } catch (error) {
            this.refreshCaptcha()
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f0f2f5;
}

.login-body {
  flex: 1;
  display: flex;
  align-items: stretch;
}

.login-left {
  flex: 1;
  background: #409EFF;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 40px;

  .bg-image {
    max-width: 85%;
    max-height: 70%;
    object-fit: contain;
  }
}

.login-right {
  width: 420px;
  flex-shrink: 0;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 50px;

  .login-form {
    width: 100%;

    .system-name {
      font-size: 22px;
      color: #304156;
      font-weight: 600;
      letter-spacing: 2px;
      margin-bottom: 50px;
      text-align: center;
    }

    .form-title {
      font-size: 18px;
      color: #304156;
      margin-bottom: 30px;
      font-weight: 500;

      i {
        margin-right: 6px;
      }
    }

    .captcha-img {
      height: 40px;
      width: 100%;
      cursor: pointer;
      border: 1px solid #dcdfe6;
      border-radius: 0;
    }

    ::v-deep .el-input__inner {
      border-radius: 0;
    }

    ::v-deep .el-button {
      border-radius: 0;
    }
  }
}
</style>
