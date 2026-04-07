import request from '@/utils/request'

export function getCourseCollegeLimitPage(params) {
  return request({ url: '/course-college-limit/page', method: 'get', params })
}

export function getCourseCollegeLimitByCourse(courseCode) {
  return request({ url: `/course-college-limit/by-course/${courseCode}`, method: 'get' })
}

export function addCourseCollegeLimit(data) {
  return request({ url: '/course-college-limit', method: 'post', data })
}

export function deleteCourseCollegeLimit(id) {
  return request({ url: `/course-college-limit/${id}`, method: 'delete' })
}
