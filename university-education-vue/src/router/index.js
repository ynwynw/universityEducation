import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决 Vue Router NavigationDuplicated / NavigationRedirected 报错
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}
const originalReplace = VueRouter.prototype.replace
VueRouter.prototype.replace = function replace(location) {
  return originalReplace.call(this, location).catch(err => err)
}

// 静态路由
export const constantRoutes = [
  { path: '/login', component: () => import('@/views/login/index.vue'), hidden: true },
  { path: '/404', component: () => import('@/views/error/404.vue'), hidden: true },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/index.vue'), meta: { title: '首页', icon: 'el-icon-s-home', affix: true } },
      { path: 'profile', name: 'Profile', component: () => import('@/views/profile/index.vue'), meta: { title: '个人中心', icon: 'el-icon-user' }, hidden: true }
    ]
  }
]

// 动态路由 - 根据权限加载
export const asyncRoutes = [
  {
    path: '/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/system/college',
    meta: { title: '系统管理', icon: 'el-icon-setting' },
    children: [
      { path: 'college', name: 'College', component: () => import('@/views/system/college/index.vue'), meta: { title: '学院管理', icon: 'el-icon-office-building' } },
      { path: 'major', name: 'Major', component: () => import('@/views/system/major/index.vue'), meta: { title: '专业管理', icon: 'el-icon-collection' } },
      { path: 'class', name: 'Class', component: () => import('@/views/system/class/index.vue'), meta: { title: '班级管理', icon: 'el-icon-s-flag' } },
      { path: 'teacher', name: 'Teacher', component: () => import('@/views/system/teacher/index.vue'), meta: { title: '教师管理', icon: 'el-icon-user' } },
      { path: 'student', name: 'Student', component: () => import('@/views/system/student/index.vue'), meta: { title: '学生管理', icon: 'el-icon-s-custom' } },
      { path: 'course', name: 'Course', component: () => import('@/views/system/course/index.vue'), meta: { title: '课程管理', icon: 'el-icon-notebook-2' } },
      { path: 'teaching-assignment', name: 'TeachingAssignment', component: () => import('@/views/system/teaching-assignment/index.vue'), meta: { title: '授课分配', icon: 'el-icon-s-cooperation' } },
      { path: 'grade', name: 'Grade', component: () => import('@/views/system/grade/index.vue'), meta: { title: '成绩管理', icon: 'el-icon-document' } },
      { path: 'grade-statistics', name: 'GradeStatistics', component: () => import('@/views/system/grade-statistics/index.vue'), meta: { title: '成绩统计', icon: 'el-icon-data-analysis' } },
      { path: 'evaluation', name: 'Evaluation', component: () => import('@/views/system/evaluation/index.vue'), meta: { title: '评教管理', icon: 'el-icon-star-off' } },
      { path: 'announcement', name: 'Announcement', component: () => import('@/views/system/announcement/index.vue'), meta: { title: '公告管理', icon: 'el-icon-bell' } },
      { path: 'teaching-material', name: 'TeachingMaterial', component: () => import('@/views/system/teaching-material/index.vue'), meta: { title: '教学资料审核', icon: 'el-icon-folder-opened' } },
      { path: 'questionnaire', name: 'Questionnaire', component: () => import('@/views/system/questionnaire/index.vue'), meta: { title: '问卷管理', icon: 'el-icon-s-order' } },
      { path: 'course-selection-config', name: 'CourseSelectionConfig', component: () => import('@/views/system/course-selection-config/index.vue'), meta: { title: '选课配置', icon: 'el-icon-time' } },
      { path: 'grade-weight-config', name: 'GradeWeightConfig', component: () => import('@/views/system/grade-weight-config/index.vue'), meta: { title: '成绩比例配置', icon: 'el-icon-s-data' } },
      { path: 'course-college-limit', name: 'CourseCollegeLimit', component: () => import('@/views/system/course-college-limit/index.vue'), meta: { title: '公共课学院限制', icon: 'el-icon-lock' } },
      { path: 'evaluation-security-config', name: 'EvaluationSecurityConfig', component: () => import('@/views/system/evaluation-security-config/index.vue'), meta: { title: '评教安全配置', icon: 'el-icon-shield' } },
      { path: 'menu', name: 'Menu', component: () => import('@/views/system/menu/index.vue'), meta: { title: '菜单管理', icon: 'el-icon-menu' } },
      { path: 'role', name: 'Role', component: () => import('@/views/system/role/index.vue'), meta: { title: '角色管理', icon: 'el-icon-s-check' } },
      { path: 'log', name: 'Log', component: () => import('@/views/system/log/index.vue'), meta: { title: '操作日志', icon: 'el-icon-document-copy' } }
    ]
  },
  {
    path: '/teacher',
    component: () => import('@/layout/index.vue'),
    redirect: '/teacher/my-courses',
    meta: { title: '教师中心', icon: 'el-icon-user' },
    children: [
      { path: 'my-courses', name: 'TeacherMyCourses', component: () => import('@/views/teacher/my-courses/index.vue'), meta: { title: '我的授课', icon: 'el-icon-notebook-2' } },
      { path: 'grade-input', name: 'TeacherGradeInput', component: () => import('@/views/teacher/grade-input/index.vue'), meta: { title: '成绩录入', icon: 'el-icon-edit-outline' } },
      { path: 'evaluation', name: 'TeacherEvaluation', component: () => import('@/views/teacher/evaluation/index.vue'), meta: { title: '教师互评', icon: 'el-icon-s-comment' } },
      { path: 'my-evaluation', name: 'TeacherMyEvaluation', component: () => import('@/views/teacher/my-evaluation/index.vue'), meta: { title: '我的评教结果', icon: 'el-icon-star-on' } },
      { path: 'teaching-material', name: 'TeacherTeachingMaterial', component: () => import('@/views/teacher/teaching-material/index.vue'), meta: { title: '教学资料', icon: 'el-icon-folder-opened' } },
      { path: 'my-info', name: 'TeacherMyInfo', component: () => import('@/views/teacher/my-info/index.vue'), meta: { title: '个人信息', icon: 'el-icon-user' } }
    ]
  },
  {
    path: '/student',
    component: () => import('@/layout/index.vue'),
    redirect: '/student/my-info',
    meta: { title: '学生中心', icon: 'el-icon-s-custom' },
    children: [
      { path: 'my-info', name: 'StudentMyInfo', component: () => import('@/views/student/my-info/index.vue'), meta: { title: '个人信息', icon: 'el-icon-user' } },
      { path: 'course-selection', name: 'StudentCourseSelection', component: () => import('@/views/student/course-selection/index.vue'), meta: { title: '选课中心', icon: 'el-icon-circle-plus-outline' } },
      { path: 'my-grades', name: 'StudentMyGrades', component: () => import('@/views/student/my-grades/index.vue'), meta: { title: '我的成绩', icon: 'el-icon-document' } },
      { path: 'evaluation', name: 'StudentEvaluation', component: () => import('@/views/student/evaluation/index.vue'), meta: { title: '学生评教', icon: 'el-icon-star-off' } }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new VueRouter({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// 重置路由
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
