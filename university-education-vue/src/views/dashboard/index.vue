<template>
  <div class="dashboard-container">
    <!-- 管理员/教师统计卡片 -->
    <el-row v-if="!isStudent" :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-label">学生总数</div>
          <div class="stat-value">{{ stats.studentCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-label">教师总数</div>
          <div class="stat-value">{{ stats.teacherCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-label">课程总数</div>
          <div class="stat-value">{{ stats.courseCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-label">学院总数</div>
          <div class="stat-value">{{ stats.collegeCount || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 学生统计卡片 -->
    <el-row v-if="isStudent" :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-label">已修学分</div>
          <div class="stat-value">{{ studentStats.totalCredits || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-label">平均绩点</div>
          <div class="stat-value">{{ studentStats.avgGpa || '-' }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success">
          <div class="stat-label">通过课程</div>
          <div class="stat-value">{{ studentStats.passCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card danger">
          <div class="stat-label">不及格课程</div>
          <div class="stat-value">{{ studentStats.failCount || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 学生成绩图表 -->
    <el-row v-if="isStudent && gradeList.length > 0" :gutter="20" style="margin-top: 20px">
      <el-col :span="8">
        <el-card>
          <div slot="header">成绩分布</div>
          <div ref="pieChart" style="height: 260px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header">各科成绩</div>
          <div ref="barChart" style="height: 260px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header">学期绩点趋势</div>
          <div ref="lineChart" style="height: 260px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 公告 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div slot="header">最新公告</div>
          <el-table :data="announcements" style="width: 100%" max-height="300" @row-click="handleAnnouncementClick" class="clickable-table">
            <el-table-column prop="title" label="标题" show-overflow-tooltip />
            <el-table-column prop="publishTime" label="发布时间" width="180" />
          </el-table>
          <div v-if="!announcements.length" style="text-align: center; color: #999; padding: 20px;">暂无公告</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 公告详情弹窗 -->
    <el-dialog title="公告详情" :visible.sync="announcementVisible" width="600px" append-to-body>
      <div v-loading="announcementLoading">
        <h3 style="margin: 0 0 10px 0;">{{ announcementDetail.title }}</h3>
        <div style="color: #999; font-size: 13px; margin-bottom: 15px;">
          <span>发布时间：{{ announcementDetail.publishTime }}</span>
          <el-tag v-if="announcementDetail.priority === 2" type="danger" size="mini" style="margin-left: 10px;">紧急</el-tag>
          <el-tag v-else-if="announcementDetail.priority === 1" type="warning" size="mini" style="margin-left: 10px;">重要</el-tag>
        </div>
        <el-divider />
        <div style="line-height: 1.8; white-space: pre-wrap;">{{ announcementDetail.content }}</div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getDashboardStats, getLatestAnnouncements } from '@/api/dashboard'
import { getAnnouncementById } from '@/api/announcement'
import { getMyGrades } from '@/api/grade'
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {},
      announcements: [],
      announcementVisible: false,
      announcementLoading: false,
      announcementDetail: {},
      studentStats: { totalCredits: 0, avgGpa: '-', passCount: 0, failCount: 0 },
      gradeList: [],
      pieChart: null,
      barChart: null,
      lineChart: null
    }
  },
  computed: {
    ...mapGetters(['roles']),
    isStudent() {
      return this.roles.includes('student')
    }
  },
  created() { this.loadData() },
  mounted() {
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    this.pieChart && this.pieChart.dispose()
    this.barChart && this.barChart.dispose()
    this.lineChart && this.lineChart.dispose()
  },
  methods: {
    initCharts() {
      this.$nextTick(() => {
        if (this.$refs.pieChart) this.pieChart = echarts.init(this.$refs.pieChart)
        if (this.$refs.barChart) this.barChart = echarts.init(this.$refs.barChart)
        if (this.$refs.lineChart) this.lineChart = echarts.init(this.$refs.lineChart)
        this.updateCharts()
      })
    },
    resizeCharts() {
      this.pieChart && this.pieChart.resize()
      this.barChart && this.barChart.resize()
      this.lineChart && this.lineChart.resize()
    },
    async handleAnnouncementClick(row) {
      this.announcementVisible = true
      this.announcementLoading = true
      this.announcementDetail = {}
      try {
        const { data } = await getAnnouncementById(row.id)
        this.announcementDetail = data || {}
      } catch (e) {
        this.$message.error('获取公告详情失败')
      } finally {
        this.announcementLoading = false
      }
    },
    async loadData() {
      try {
        const [statsRes, announcementRes] = await Promise.all([
          getDashboardStats(),
          getLatestAnnouncements()
        ])
        this.stats = statsRes.data || {}
        this.announcements = announcementRes.data || []
        
        if (this.isStudent) {
          await this.loadStudentGrades()
        }
      } catch (e) {
        console.error('加载仪表盘数据失败', e)
      }
    },
    async loadStudentGrades() {
      try {
        const { data } = await getMyGrades()
        this.gradeList = data || []
        this.calcStudentStats()
        if (this.gradeList.length > 0) {
          this.$nextTick(() => this.initCharts())
        }
      } catch (e) { console.error(e) }
    },
    calcStudentStats() {
      let totalCredits = 0, passCount = 0, failCount = 0, totalGpa = 0
      this.gradeList.forEach(item => {
        if (item.totalScore >= 60 || item.gradeStatus === 2) {
          totalCredits += item.credit || 0
          passCount++
          totalGpa += this.scoreToGpa(item.totalScore) * (item.credit || 0)
        } else { failCount++ }
      })
      this.studentStats = {
        totalCredits, passCount, failCount,
        avgGpa: totalCredits > 0 ? (totalGpa / totalCredits).toFixed(2) : '-'
      }
    },
    updateCharts() {
      this.updatePieChart()
      this.updateBarChart()
      this.updateLineChart()
    },
    updatePieChart() {
      if (!this.pieChart) return
      const ranges = [
        { name: '优秀(90-100)', min: 90, max: 100, color: '#67C23A' },
        { name: '良好(80-89)', min: 80, max: 89, color: '#409EFF' },
        { name: '中等(70-79)', min: 70, max: 79, color: '#E6A23C' },
        { name: '及格(60-69)', min: 60, max: 69, color: '#909399' },
        { name: '不及格(<60)', min: 0, max: 59, color: '#F56C6C' }
      ]
      const pieData = ranges.map(r => ({
        name: r.name, value: this.gradeList.filter(g => g.totalScore >= r.min && g.totalScore <= r.max).length,
        itemStyle: { color: r.color }
      })).filter(d => d.value > 0)
      this.pieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}门 ({d}%)' },
        series: [{ type: 'pie', radius: ['40%', '70%'], data: pieData, label: { show: true, formatter: '{b}\n{c}门', fontSize: 10 } }]
      })
    },
    updateBarChart() {
      if (!this.barChart) return
      const courses = this.gradeList.slice(0, 8)
      this.barChart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: courses.map(c => c.courseName), axisLabel: { rotate: 30, fontSize: 9 } },
        yAxis: { type: 'value', min: 0, max: 100 },
        series: [{
          type: 'bar', data: courses.map(c => ({
            value: c.totalScore,
            itemStyle: { color: c.totalScore >= 90 ? '#67C23A' : c.totalScore >= 60 ? '#409EFF' : '#F56C6C' }
          })),
          label: { show: true, position: 'top', fontSize: 9 }
        }],
        grid: { left: 40, right: 10, bottom: 60, top: 20 }
      })
    },
    updateLineChart() {
      if (!this.lineChart) return
      const semesterGpa = {}
      this.gradeList.forEach(g => {
        if (!semesterGpa[g.semester]) semesterGpa[g.semester] = { total: 0, credits: 0 }
        if (g.totalScore >= 60) {
          semesterGpa[g.semester].total += this.scoreToGpa(g.totalScore) * (g.credit || 0)
          semesterGpa[g.semester].credits += g.credit || 0
        }
      })
      const semesters = Object.keys(semesterGpa).sort()
      const gpaData = semesters.map(s => semesterGpa[s].credits > 0 ? (semesterGpa[s].total / semesterGpa[s].credits).toFixed(2) : 0)
      this.lineChart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: semesters.map(s => this.formatSemester(s)), axisLabel: { rotate: 20, fontSize: 9 } },
        yAxis: { type: 'value', min: 0, max: 4.5 },
        series: [{ type: 'line', data: gpaData, smooth: true, itemStyle: { color: '#409EFF' }, areaStyle: { color: 'rgba(64,158,255,0.2)' }, label: { show: true, fontSize: 9 } }],
        grid: { left: 40, right: 10, bottom: 60, top: 20 }
      })
    },
    scoreToGpa(score) {
      if (score >= 90) return 4.0
      if (score >= 85) return 3.7
      if (score >= 80) return 3.3
      if (score >= 75) return 3.0
      if (score >= 70) return 2.7
      if (score >= 65) return 2.3
      if (score >= 60) return 2.0
      return 0
    },
    formatSemester(semester) {
      if (!semester) return ''
      const parts = semester.split('-')
      if (parts.length === 3) return `${parts[0].slice(2)}-${parts[1].slice(2)}${parts[2] === '1' ? '上' : '下'}`
      return semester
    }
  }
}
</script>
<style lang="scss" scoped>
.dashboard-container {
  .stat-card {
    ::v-deep .el-card__body { text-align: center; padding: 20px; }
    .stat-label { font-size: 14px; color: #999; margin-bottom: 8px; }
    .stat-value { font-size: 32px; font-weight: bold; color: #333; }
    &.success .stat-value { color: #67C23A; }
    &.danger .stat-value { color: #F56C6C; }
  }
  .clickable-table {
    ::v-deep .el-table__body tr {
      cursor: pointer;
      &:hover > td { color: #409EFF; }
    }
  }
}
</style>
