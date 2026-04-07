/**
 * 生成学期列表
 */
export function generateSemesterList(startYear = 2020) {
  const now = new Date()
  const currentYear = now.getFullYear()
  const currentMonth = now.getMonth() + 1
  // 9月开始算新学年
  const endYear = currentMonth >= 9 ? currentYear + 1 : currentYear
  const list = []
  for (let year = startYear; year < endYear; year++) {
    list.push(`${year}-${year + 1}-1`)
    list.push(`${year}-${year + 1}-2`)
  }
  return list.reverse()
}

/**
 * 获取当前学期
 */
export function getCurrentSemester() {
  const now = new Date()
  const year = now.getFullYear()
  const month = now.getMonth() + 1
  if (month >= 9) {
    return `${year}-${year + 1}-1`
  } else if (month >= 2) {
    return `${year - 1}-${year}-2`
  } else {
    return `${year - 1}-${year}-1`
  }
}
