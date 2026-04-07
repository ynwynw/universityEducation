<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="user-card">
          <div class="user-info">
            <el-upload class="avatar-uploader" :action="uploadUrl" :headers="uploadHeaders" :show-file-list="false"
              :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
              <el-avatar :size="100" :src="avatarUrl" />
              <div class="upload-tip">点击更换头像</div>
            </el-upload>
            <h3>{{ profileData.name || userInfo.realName || userInfo.username }}</h3>
            <p class="role-tag">
              <el-tag v-if="isAdmin" type="danger">管理员</el-tag>
              <el-tag v-else-if="isTeacher" type="success">教师</el-tag>
              <el-tag v-else-if="isStudent" type="primary">学生</el-tag>
            </p>
            <p v-if="isTeacher || isStudent" class="user-no">{{ profileData.teacherNo || profileData.studentNo }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <div slot="header">
            <span>基本信息</span>
            <el-button v-if="!editing" type="text" style="float: right" @click="editing = true">编辑</el-button>
            <span v-else style="float: right">
              <el-button type="text" @click="cancelEdit">取消</el-button>
              <el-button type="text" @click="saveProfile">保存</el-button>
            </span>
          </div>
          <el-form ref="form" :model="form" label-width="100px" :disabled="!editing">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="姓名">
                  <el-input v-model="form.name" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="性别">
                  <el-radio-group v-model="form.gender" :disabled="true">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="联系电话">
                  <el-input v-model="form.phone" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱">
                  <el-input v-model="form.email" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 教师特有字段 -->
            <template v-if="isTeacher">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="所属学院">
                    <el-input v-model="form.collegeName" :disabled="true" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="职称">
                    <el-input v-model="form.title" :disabled="true" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="学历">
                    <el-input v-model="form.education" :disabled="true" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="研究方向">
                    <el-input v-model="form.majorStudied" :disabled="true" />
                  </el-form-item>
                </el-col>
              </el-row>
            </template>
            <!-- 学生特有字段 -->
            <template v-if="isStudent">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="所属学院">
                    <el-input v-model="form.collegeName" :disabled="true" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="专业">
                    <el-input v-model="form.majorName" :disabled="true" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="班级">
                    <el-input v-model="form.className" :disabled="true" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="入学年份">
                    <el-input v-model="form.enrollmentYear" :disabled="true" />
                  </el-form-item>
                </el-col>
              </el-row>
            </template>
          </el-form>
        </el-card>
        <el-card style="margin-top: 20px">
          <div slot="header">修改密码</div>
          <el-form ref="pwdForm" :model="pwdForm" :rules="pwdRules" label-width="100px">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="changePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'
import { getMyProfile, updateMyProfile } from '@/api/profile'
import { changePassword } from '@/api/auth'

export default {
  name: 'Profile',
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.pwdForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      profileData: {},
      form: {},
      editing: false,
      pwdForm: { oldPassword: '', newPassword: '', confirmPassword: '' },
      pwdRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请确认新密码', trigger: 'blur' }, { validator: validateConfirm, trigger: 'blur' }]
      },
      baseUrl: '/api',
      uploadUrl: '/api/file/upload?type=avatar',
      uploadHeaders: { Authorization: getToken() },
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  computed: {
    ...mapGetters(['roles', 'userInfo']),
    isAdmin() { return this.roles.includes('admin') },
    isTeacher() { return this.roles.includes('teacher') },
    isStudent() { return this.roles.includes('student') },
    avatarUrl() {
      if (this.form.avatar) return this.baseUrl + '/file/image/' + this.form.avatar
      return this.defaultAvatar
    }
  },
  created() { this.loadProfile() },
  methods: {
    async loadProfile() {
      try {
        const { data } = await getMyProfile()
        this.profileData = data || {}
        this.form = { ...this.profileData }
      } catch (e) {
        console.error('加载个人信息失败', e)
      }
    },
    cancelEdit() {
      this.form = { ...this.profileData }
      this.editing = false
    },
    async saveProfile() {
      try {
        await updateMyProfile({ phone: this.form.phone, email: this.form.email, avatar: this.form.avatar })
        this.$message.success('保存成功')
        this.profileData = { ...this.form }
        this.editing = false
      } catch (e) {
        this.$message.error('保存失败')
      }
    },
    handleAvatarSuccess(res) {
      if (res.code === 200) {
        this.form.avatar = res.data.fileId
        this.$message.success('头像上传成功，请点击保存')
        this.editing = true
      } else {
        this.$message.error(res.msg || '上传失败')
      }
    },
    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isImage) this.$message.error('只能上传图片文件!')
      if (!isLt2M) this.$message.error('图片大小不能超过 2MB!')
      return isImage && isLt2M
    },
    async changePassword() {
      await this.$refs.pwdForm.validate()
      try {
        await changePassword({ oldPassword: this.pwdForm.oldPassword, newPassword: this.pwdForm.newPassword })
        this.$message.success('密码修改成功，请重新登录')
        this.$refs.pwdForm.resetFields()
        await this.$store.dispatch('user/logout')
        this.$router.push('/login')
      } catch (e) {
        this.$message.error(e.message || '密码修改失败')
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.profile-container {
  padding: 20px;
  .user-card {
    .user-info {
      text-align: center;
      padding: 20px 0;
      .avatar-uploader {
        cursor: pointer;
        .upload-tip { font-size: 12px; color: #999; margin-top: 8px; }
        &:hover .upload-tip { color: #409EFF; }
      }
      h3 { margin: 15px 0 10px; }
      .role-tag { margin-bottom: 5px; }
      .user-no { color: #999; font-size: 14px; }
    }
  }
}
</style>
