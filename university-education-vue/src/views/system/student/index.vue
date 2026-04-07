<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
      <el-form-item label="学院">
        <el-select v-model="queryParams.collegeId" placeholder="请选择学院" clearable @change="onCollegeChange">
          <el-option v-for="item in collegeList" :key="item.id" :label="item.collegeName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="专业">
        <el-select v-model="queryParams.majorId" placeholder="请选择专业" clearable @change="onMajorChange">
          <el-option v-for="item in majorList" :key="item.id" :label="item.majorName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="班级">
        <el-select v-model="queryParams.classId" placeholder="请选择班级" clearable>
          <el-option v-for="item in classList" :key="item.id" :label="item.className" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="学号">
        <el-input v-model="queryParams.studentNo" placeholder="请输入学号" clearable />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="table-container">
      <div class="table-toolbar">
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
          <el-button type="warning" icon="el-icon-document" @click="handleDownloadTemplate">下载导入模板</el-button>
          <el-upload
            ref="importUpload"
            :action="''"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleImportChange"
            accept=".xlsx,.xls"
            style="display: inline-block; margin-left: 10px;"
          >
            <el-button type="success" icon="el-icon-upload2">导入学生</el-button>
          </el-upload>
        </div>
        <el-button type="danger" icon="el-icon-delete" :disabled="!selectedIds.length" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="头像" width="70" align="center">
          <template slot-scope="scope">
            <el-avatar :size="40" :src="scope.row.avatar ? baseUrl + '/file/image/' + scope.row.avatar : defaultAvatar" />
          </template>
        </el-table-column>
        <el-table-column label="学号" prop="studentNo" width="120" />
        <el-table-column label="姓名" prop="name" width="100" />
        <el-table-column label="性别" prop="gender" width="60" align="center">
          <template slot-scope="scope">{{ scope.row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column label="学院" prop="collegeName" width="150" />
        <el-table-column label="专业" prop="majorName" width="150" />
        <el-table-column label="班级" prop="className" width="150" />
        <el-table-column label="联系电话" prop="phone" width="120" />
        <el-table-column label="状态" prop="status" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '在读' : '离校' }}</el-tag>
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
                <el-form-item label="学号" prop="studentNo">
                  <el-input v-model="form.studentNo" placeholder="请输入学号" :disabled="!!form.id" />
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
            <el-form-item label="入学年份" prop="enrollmentYear">
              <el-date-picker v-model="form.enrollmentYear" type="year" placeholder="选择年份" style="width: 100%" value-format="yyyy" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学院" prop="collegeId">
              <el-select v-model="form.collegeId" placeholder="请选择学院" style="width: 100%" @change="onFormCollegeChange" :disabled="!!form.id">
                <el-option v-for="item in collegeList" :key="item.id" :label="item.collegeName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="majorId">
              <el-select v-model="form.majorId" placeholder="请选择专业" style="width: 100%" @change="onFormMajorChange" :disabled="!!form.id">
                <el-option v-for="item in formMajorList" :key="item.id" :label="item.majorName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="班级" prop="classId">
              <el-select v-model="form.classId" placeholder="请选择班级" style="width: 100%" :disabled="!!form.id">
                <el-option v-for="item in formClassList" :key="item.id" :label="item.className" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">在读</el-radio>
                <el-radio :label="0">离校</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getStudentPage, addStudent, updateStudent, deleteStudent, batchDeleteStudent, downloadStudentTemplate, importStudents } from '@/api/student'
import { getCollegeList } from '@/api/college'
import { getMajorList } from '@/api/major'
import { getClassList } from '@/api/class'
import Pagination from '@/components/Pagination'
import { getToken } from '@/utils/auth'

export default {
  name: 'Student',
  components: { Pagination },
  data() {
    return {
      loading: false, tableData: [], total: 0, selectedIds: [], dialogVisible: false, dialogTitle: '',
      collegeList: [], majorList: [], classList: [], formMajorList: [], formClassList: [],
      queryParams: { pageNum: 1, pageSize: 10, collegeId: null, majorId: null, classId: null, studentNo: '', name: '' },
      form: {},
      rules: {
        studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        collegeId: [{ required: true, message: '请选择学院', trigger: 'change' }],
        majorId: [{ required: true, message: '请选择专业', trigger: 'change' }],
        classId: [{ required: true, message: '请选择班级', trigger: 'change' }]
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
    async onCollegeChange(id) { this.queryParams.majorId = null; this.queryParams.classId = null; this.majorList = []; this.classList = []; if (id) { const { data } = await getMajorList(id); this.majorList = data } },
    async onMajorChange(id) { this.queryParams.classId = null; this.classList = []; if (id) { const { data } = await getClassList(id); this.classList = data } },
    async onFormCollegeChange(id) { this.form.majorId = null; this.form.classId = null; this.formMajorList = []; this.formClassList = []; if (id) { const { data } = await getMajorList(id); this.formMajorList = data } },
    async onFormMajorChange(id) { this.form.classId = null; this.formClassList = []; if (id) { const { data } = await getClassList(id); this.formClassList = data } },
    async getList() { this.loading = true; try { const { data } = await getStudentPage(this.queryParams); this.tableData = data.list; this.total = data.total } finally { this.loading = false } },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.majorList = []; this.classList = []; this.handleQuery() },
    handleSelectionChange(selection) { this.selectedIds = selection.map(item => item.id) },
    handleAdd() { this.dialogTitle = '新增学生'; this.form = { status: 1, gender: 1 }; this.formMajorList = []; this.formClassList = []; this.dialogVisible = true },
    async handleEdit(row) { 
      this.dialogTitle = '编辑学生'; this.form = { ...row }
      if (row.collegeId) { const { data } = await getMajorList(row.collegeId); this.formMajorList = data }
      if (row.majorId) { const { data } = await getClassList(row.majorId); this.formClassList = data }
      this.dialogVisible = true 
    },
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
      if (this.form.id) { await updateStudent(this.form) } else { await addStudent(this.form) }
      this.$message.success('操作成功'); this.dialogVisible = false; this.getList() 
    },
    async handleDelete(row) { await this.$confirm('确定要删除该学生吗？', '提示', { type: 'warning' }); await deleteStudent(row.id); this.$message.success('删除成功'); this.getList() },
    async handleBatchDelete() { await this.$confirm(`确定要删除选中的 ${this.selectedIds.length} 条数据吗？`, '提示', { type: 'warning' }); await batchDeleteStudent(this.selectedIds); this.$message.success('删除成功'); this.getList() },
    async handleDownloadTemplate() {
      try {
        const res = await downloadStudentTemplate()
        const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '学生导入模板.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
      } catch (e) {
        this.$message.error('下载模板失败')
      }
    },
    async handleImportChange(file) {
      try {
        const { data } = await importStudents(file.raw)
        let msg = `成功导入 ${data.successCount} 名学生`
        if (data.errorCount > 0) {
          msg += `，失败 ${data.errorCount} 条`
          if (data.errors && data.errors.length > 0) {
            msg += '：\n' + data.errors.join('\n')
          }
        }
        this.$alert(msg, '导入结果', { type: data.errorCount > 0 ? 'warning' : 'success' })
        this.getList()
      } catch (e) {
        this.$message.error('导入失败')
      }
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
