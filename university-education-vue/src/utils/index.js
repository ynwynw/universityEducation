/**
 * 日期格式化
 */
export function formatDate(date, fmt = 'yyyy-MM-dd HH:mm:ss') {
  if (!date) return ''
  if (typeof date === 'string') {
    date = new Date(date)
  }
  const o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'H+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds(),
    'q+': Math.floor((date.getMonth() + 3) / 3),
    'S': date.getMilliseconds()
  }
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  for (let k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  return fmt
}

/**
 * 深拷贝
 */
export function deepClone(source) {
  if (!source || typeof source !== 'object') {
    return source
  }
  const target = Array.isArray(source) ? [] : {}
  for (const key in source) {
    if (Object.prototype.hasOwnProperty.call(source, key)) {
      target[key] = typeof source[key] === 'object' ? deepClone(source[key]) : source[key]
    }
  }
  return target
}

/**
 * 构建树形结构
 */
export function buildTree(data, idField = 'id', parentField = 'parentId', childrenField = 'children') {
  const map = {}
  const tree = []
  
  data.forEach(item => {
    map[item[idField]] = { ...item, [childrenField]: [] }
  })
  
  data.forEach(item => {
    const parent = map[item[parentField]]
    if (parent) {
      parent[childrenField].push(map[item[idField]])
    } else {
      tree.push(map[item[idField]])
    }
  })
  
  return tree
}

/**
 * 下载文件
 */
export function downloadFile(blob, filename) {
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)
}
