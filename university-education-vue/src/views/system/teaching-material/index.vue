<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
      <el-form-item label="标题">
        <el-input v-model="queryParams.title" placeholder="请输入标题" clearable />
      </el-form-item>
      <el-form-item label="审核状态">
        <el-select v-model="queryParams.auditStatus" placeholder="请选择" clearable>
          <el-option label="待审核" :value="0" />
          <el-option label="已通过" :value="1" />
          <el-option label="已拒绝" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="资料类型">
        <el-select v-model="queryParams.materialType" placeholder="请选择" clearable>
          <el-option label="教案" value="LESSON_PLAN" />
          <el-option label="授课计划" value="SYLLABUS" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableData">
      <el-table-column label="资料标题" prop="title" min-width="180" show-overflow-tooltip />
      <el-table-column label="资料类型" width="100" align="center">
        <template slot-scope="scope">{{ typeMap[scope.row.materialType] || scope.row.materialType }}</template>
      </el-table-column>
      <el-table-column label="关联课程" prop="courseName" width="160" show-overflow-tooltip />
      <el-table-column label="上传教师" prop="teacherName" width="100" />
      <el-table-column label="文件名" prop="fileName" width="180" show-overflow-tooltip />
      <el-table-column label="审核状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="statusTagType(scope.row.auditStatus)">{{ statusMap[scope.row.auditStatus] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="审核人" prop="auditorName" width="100" />
      <el-table-column label="审核意见" prop="auditRemark" width="160" show-overflow-tooltip />
      <el-table-column label="上传时间" prop="createTime" width="160" />
      <el-table-column label="操作" width="220" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-download" @click="handleDownload(scope.row)">下载</el-button>
          <el-button type="text" icon="el-icon-view" @click="handlePreview(scope.row)">预览</el-button>
          <el-button v-if="scope.row.auditStatus === 0" type="text" icon="el-icon-check" @click="handleAudit(scope.row)">审核</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 文件预览组件 -->
    <file-preview ref="filePreview" />

    <!-- 审核弹窗 -->
    <el-dialog title="审核教学资料" :visible.sync="auditVisible" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="100px">
        <el-form-item label="资料标题">
          <span>{{ auditForm.title }}</span>
        </el-form-item>
        <el-form-item label="审核结果" required>
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input v-model="auditForm.auditRemark" type="textarea" :rows="3" placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="auditVisible = false">取 消</el-button>
        <el-button type="primary" :loading="auditLoading" @click="submitAudit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getMaterialPage, auditMaterial } from '@/api/teachingMaterial'
import Pagination from '@/components/Pagination'
import FilePreview from '@/components/FilePreview'

export default {
  name: 'TeachingMaterialManage',
  components: { Pagination, FilePreview },
  data() {
    return {
      loading: false,
      auditLoading: false,
      tableData: [],
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, title: '', auditStatus: null, materialType: '' },
      typeMap: { LESSON_PLAN: '教案', SYLLABUS: '授课计划', OTHER: '其他' },
      statusMap: { 0: '待审核', 1: '已通过', 2: '已拒绝' },
      auditVisible: false,
      auditForm: { id: null, title: '', auditStatus: 1, auditRemark: '' }
    }
  },
  created() { this.getList() },
  methods: {
    statusTagType(status) {
      return { 0: 'warning', 1: 'success', 2: 'danger' }[status] || 'info'
    },
    async getList() {
      this.loading = true
      try {
        const { data } = await getMaterialPage(this.queryParams)
        this.tableData = data.list
        this.total = data.total
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryParams = { pageNum: 1, pageSize: 10, title: '', auditStatus: null, materialType: '' }; this.getList() },
    handleDownload(row) {
      window.open(`/api/teaching-material/download/${row.id}`, '_blank')
    },
    handlePreview(row) {
      this.$refs.filePreview.open(row)
    },
    handleAudit(row) {
      this.auditForm = { id: row.id, title: row.title, auditStatus: 1, auditRemark: '' }
      this.auditVisible = true
    },
    async submitAudit() {
      if (!this.auditForm.auditStatus) {
        return this.$message.warning('请选择审核结果')
      }
      this.auditLoading = true
      try {
        await auditMaterial(this.auditForm.id, this.auditForm.auditStatus, this.auditForm.auditRemark)
        this.$message.success('审核成功')
        this.auditVisible = false
        this.getList()
      } finally {
        this.auditLoading = false
      }
    }
  }
}
</script>
<style lang="scss" scoped>.danger-btn { color: #F56C6C; }</style>
