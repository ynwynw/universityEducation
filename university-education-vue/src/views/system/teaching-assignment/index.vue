<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
      <el-form-item label="学期">
        <el-select v-model="queryParams.semester" placeholder="请选择学期" clearable filterable style="width: 180px">
          <el-option v-for="item in semesterList" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="课程">
        <el-select v-model="queryParams.courseId" placeholder="请选择" clearable filterable>
          <el-option v-for="item in courseList" :key="item.id" :label="item.courseName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="教师">
        <el-select v-model="queryParams.teacherId" placeholder="请选择" clearable filterable>
          <el-option v-for="item in teacherList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
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
        <el-table-column label="学期" prop="semester" width="130" />
        <el-table-column label="课程代码" prop="course_code" width="120" />
        <el-table-column label="课程名称" prop="course_name" min-width="150" show-overflow-tooltip />
        <el-table-column label="课程类型" prop="course_type" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCourseTypeTag(scope.row.course_type)">
              {{ getCourseTypeLabel(scope.row.course_type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="教师工号" prop="teacher_no" width="100" />
        <el-table-column label="教师姓名" prop="teacher_name" width="100" />
        <el-table-column label="班级" prop="class_name" width="150" />
        <el-table-column label="操作" width="100" align="center">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-delete" class="danger-btn" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
    <el-dialog title="新增授课分配" :visible.sync="dialogVisible" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="学期" prop="semester">
          <el-select v-model="form.semester" placeholder="请选择学期" filterable style="width: 100%" @change="onSemesterChange">
            <el-option v-for="item in semesterList" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请先选择学期" filterable style="width: 100%" @change="onCourseChange">
            <el-option v-for="item in formCourseList" :key="item.id" :label="`${item.courseCode} - ${item.courseName}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="教师" prop="teacherId">
          <el-select v-model="form.teacherId" placeholder="请先选择课程" filterable style="width: 100%">
            <el-option v-for="item in formTeacherList" :key="item.id" :label="`${item.teacherNo} - ${item.name}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="classId">
          <el-select v-model="form.classId" placeholder="请选择班级" filterable style="width: 100%">
            <el-option v-for="item in classList" :key="item.id" :label="item.className" :value="item.id" />
          </el-select>
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
import { getTeachingAssignmentPage, addTeachingAssignment, deleteTeachingAssignment, batchDeleteTeachingAssignment } from '@/api/teachingAssignment'
import { getCoursePage } from '@/api/course'
import { getTeacherPage, getTeacherList } from '@/api/teacher'
import { getClassPage } from '@/api/class'
import { generateSemesterList } from '@/utils/semester'
import Pagination from '@/components/Pagination'
export default {
  name: 'TeachingAssignment',
  components: { Pagination },
  data() {
    return {
      loading: false, tableData: [], total: 0, selectedIds: [], dialogVisible: false,
      courseList: [], teacherList: [], classList: [], semesterList: generateSemesterList(),
      formCourseList: [], formTeacherList: [],
      queryParams: { pageNum: 1, pageSize: 10, semester: '', courseId: null, teacherId: null },
      form: {},
      rules: {
        semester: [{ required: true, message: '请选择学期', trigger: 'change' }],
        courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
        teacherId: [{ required: true, message: '请选择教师', trigger: 'change' }],
        classId: [{ required: true, message: '请选择班级', trigger: 'change' }]
      }
    }
  },
  created() { this.getList(); this.loadOptions() },
  methods: {
    async getList() {
      this.loading = true
      try {
        const { data } = await getTeachingAssignmentPage(this.queryParams)
        this.tableData = data.list; this.total = data.total
      } finally { this.loading = false }
    },
    async loadOptions() {
      const [courses, teachers, classes] = await Promise.all([
        getCoursePage({ pageNum: 1, pageSize: 1000 }),
        getTeacherPage({ pageNum: 1, pageSize: 1000 }),
        getClassPage({ pageNum: 1, pageSize: 1000 })
      ])
      this.courseList = courses.data.list
      this.teacherList = teachers.data.list
      this.classList = classes.data.list
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.handleQuery() },
    handleSelectionChange(selection) { this.selectedIds = selection.map(item => item.id) },
    handleAdd() { this.form = { semester: '', courseId: null, teacherId: null, classId: null }; this.formCourseList = []; this.formTeacherList = []; this.dialogVisible = true },
    onSemesterChange(val) {
      this.form.courseId = null
      this.form.teacherId = null
      this.formTeacherList = []
      if (val) {
        this.formCourseList = this.courseList.filter(c => c.semester === val)
      } else {
        this.formCourseList = []
      }
    },
    async onCourseChange(val) {
      this.form.teacherId = null
      if (val) {
        const course = this.courseList.find(c => c.id === val)
        if (course && course.collegeId) {
          const { data } = await getTeacherList(course.collegeId)
          this.formTeacherList = data
        } else {
          this.formTeacherList = this.teacherList
        }
      } else {
        this.formTeacherList = []
      }
    },
    async submitForm() {
      await this.$refs.form.validate()
      await addTeachingAssignment(this.form)
      this.$message.success('操作成功'); this.dialogVisible = false; this.getList()
    },
    async handleDelete(row) {
      await this.$confirm('确定要删除该授课分配吗？', '提示', { type: 'warning' })
      await deleteTeachingAssignment(row.id)
      this.$message.success('删除成功'); this.getList()
    },
    async handleBatchDelete() {
      await this.$confirm(`确定要删除选中的 ${this.selectedIds.length} 条数据吗？`, '提示', { type: 'warning' })
      await batchDeleteTeachingAssignment(this.selectedIds)
      this.$message.success('删除成功'); this.getList()
    },
    getCourseTypeLabel(type) {
      const map = {
        'REQUIRED': '必修',
        'ELECTIVE': '选修'
      }
      return map[type] || type
    },
    getCourseTypeTag(type) {
      const map = {
        'REQUIRED': 'primary',
        'ELECTIVE': 'success'
      }
      return map[type] || 'info'
    }
  }
}
</script>
<style lang="scss" scoped>.danger-btn { color: #F56C6C; }</style>
