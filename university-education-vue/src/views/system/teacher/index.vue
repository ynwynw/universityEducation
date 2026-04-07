<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
      <el-form-item label="学院" prop="collegeId">
        <el-select v-model="queryParams.collegeId" placeholder="请选择学院" clearable>
          <el-option v-for="item in collegeList" :key="item.id" :label="item.collegeName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="工号" prop="teacherNo">
        <el-input v-model="queryParams.teacherNo" placeholder="请输入工号" clearable />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="table-container">
      <div class="table-toolbar">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="el-icon-delete" :disabled="!selectedIds.length" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="头像" width="70" align="center">
          <template slot-scope="scope">
            <el-avatar :size="40" :src="scope.row.avatar ? baseUrl + '/file/image/' + scope.row.avatar : defaultAvatar" />
          </template>
        </el-table-column>
        <el-table-column label="工号" prop="teacherNo" width="120" />
        <el-table-column label="姓名" prop="name" width="100" />
        <el-table-column label="性别" prop="gender" width="60" align="center">
          <template slot-scope="scope">{{ scope.row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column label="所属学院" prop="collegeName" width="150" />
        <el-table-column label="职称" prop="title" width="100" />
        <el-table-column label="学历" prop="education" width="80" />
        <el-table-column label="联系电话" prop="phone" width="120" />
        <el-table-column label="状态" prop="status" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '在职' : '离职' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" class="danger-btn" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="头像">
              <el-upload class="avatar-uploader" :action="uploadUrl" :headers="uploadHeaders" :show-file-list="false"
                :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                <img v-if="form.avatar" :src="baseUrl + '/file/image/' + form.avatar" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="工号" prop="teacherNo">
                  <el-input v-model="form.teacherNo" placeholder="请输入工号" :disabled="!!form.id" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="form.name" placeholder="请输入姓名" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="form.gender">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="出生日期" prop="birthDate">
                  <el-date-picker v-model="form.birthDate" type="date" placeholder="选择日期" style="width: 100%" value-format="yyyy-MM-dd" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属学院" prop="collegeId">
              <el-select v-model="form.collegeId" placeholder="请选择学院" style="width: 100%">
                <el-option v-for="item in collegeList" :key="item.id" :label="item.collegeName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职称" prop="title">
              <el-select v-model="form.title" placeholder="请选择职称" style="width: 100%">
                <el-option label="助教" value="助教" />
                <el-option label="讲师" value="讲师" />
                <el-option label="副教授" value="副教授" />
                <el-option label="教授" value="教授" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历" prop="education">
              <el-select v-model="form.education" placeholder="请选择学历" style="width: 100%">
                <el-option label="本科" value="本科" />
                <el-option label="硕士" value="硕士" />
                <el-option label="博士" value="博士" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="研究方向" prop="majorStudied">
              <el-input v-model="form.majorStudied" placeholder="请输入研究方向" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期" prop="entryDate">
              <el-date-picker v-model="form.entryDate" type="date" placeholder="选择日期" style="width: 100%" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">在职</el-radio>
            <el-radio :label="0">离职</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getTeacherPage, addTeacher, updateTeacher, deleteTeacher, batchDeleteTeacher } from '@/api/teacher'
import { getCollegeList } from '@/api/college'
import Pagination from '@/components/Pagination'
import { getToken } from '@/utils/auth'

export default {
  name: 'Teacher',
  components: { Pagination },
  data() {
    return {
      loading: false, tableData: [], total: 0, selectedIds: [], dialogVisible: false, dialogTitle: '',
      collegeList: [],
      queryParams: { pageNum: 1, pageSize: 10, collegeId: null, teacherNo: '', name: '' },
      form: {},
      rules: {
        teacherNo: [{ required: true, message: '请输入工号', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        collegeId: [{ required: true, message: '请选择学院', trigger: 'change' }]
      },
      baseUrl: '/api',
      uploadUrl: '/api/file/upload?type=avatar',
      uploadHeaders: { Authorization: getToken() },
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  created() { this.getColleges(); this.getList() },
  methods: {
    async getColleges() { const { data } = await getCollegeList(); this.collegeList = data },
    async getList() {
      this.loading = true
      try { const { data } = await getTeacherPage(this.queryParams); this.tableData = data.list; this.total = data.total }
      finally { this.loading = false }
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.handleQuery() },
    handleSelectionChange(selection) { this.selectedIds = selection.map(item => item.id) },
    handleAdd() { this.dialogTitle = '新增教师'; this.form = { status: 1, gender: 1 }; this.dialogVisible = true },
    handleEdit(row) { this.dialogTitle = '编辑教师'; this.form = { ...row }; this.dialogVisible = true },
    handleAvatarSuccess(res) {
      if (res.code === 200) {
        this.form.avatar = res.data.fileId
        this.$message.success('头像上传成功')
      } else {
        this.$message.error(res.msg || '上传失败')
      }
    },
    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isImage) { this.$message.error('只能上传图片文件!') }
      if (!isLt2M) { this.$message.error('图片大小不能超过 2MB!') }
      return isImage && isLt2M
    },
    async submitForm() {
      await this.$refs.form.validate()
      if (this.form.id) { await updateTeacher(this.form) } else { await addTeacher(this.form) }
      this.$message.success('操作成功'); this.dialogVisible = false; this.getList()
    },
    async handleDelete(row) {
      await this.$confirm('确定要删除该教师吗？', '提示', { type: 'warning' })
      await deleteTeacher(row.id); this.$message.success('删除成功'); this.getList()
    },
    async handleBatchDelete() {
      await this.$confirm(`确定要删除选中的 ${this.selectedIds.length} 条数据吗？`, '提示', { type: 'warning' })
      await batchDeleteTeacher(this.selectedIds); this.$message.success('删除成功'); this.getList()
    }
  }
}
</script>
<style lang="scss" scoped>
.danger-btn { color: #F56C6C; }
.avatar-uploader {
  .avatar { width: 100px; height: 100px; display: block; border-radius: 6px; }
  ::v-deep .el-upload {
    border: 1px dashed #d9d9d9; border-radius: 6px; cursor: pointer; position: relative; overflow: hidden;
    &:hover { border-color: #409EFF; }
  }
  .avatar-uploader-icon { font-size: 28px; color: #8c939d; width: 100px; height: 100px; line-height: 100px; text-align: center; }
}
</style>
