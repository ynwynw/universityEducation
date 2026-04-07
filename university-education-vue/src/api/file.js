import request from '@/utils/request'

export function uploadFile(file, type) {
  const formData = new FormData()
  formData.append('file', file)
  if (type) formData.append('type', type)
  return request({ 
    url: '/file/upload', 
    method: 'post', 
    data: formData, 
    headers: { 'Content-Type': 'multipart/form-data' } 
  })
}

export function getPreviewInfo(fileId) {
  return request({ url: `/file/preview/${fileId}`, method: 'get' })
}

export function deleteFile(fileId) {
  return request({ url: `/file/${fileId}`, method: 'delete' })
}

export function getDownloadUrl(fileId) {
  return `${process.env.VUE_APP_BASE_API}/file/download/${fileId}`
}

export function getViewUrl(fileId) {
  return `${process.env.VUE_APP_BASE_API}/file/view/${fileId}`
}

export function getImageUrl(fileId) {
  return `${process.env.VUE_APP_BASE_API}/file/image/${fileId}`
}

/**
 * 预览文件
 */
export async function previewFile(fileId) {
  try {
    const { data } = await getPreviewInfo(fileId)
    if (data.previewType === 'inline') {
      // PDF/图片/txt 直接在新窗口打开
      window.open(getViewUrl(fileId), '_blank')
    } else {
      // Office等文件提示下载
      const confirmMsg = `该文件类型(${data.ext})不支持在线预览，是否下载？`
      if (window.confirm(confirmMsg)) {
        window.location.href = getDownloadUrl(fileId)
      }
    }
  } catch (e) {
    console.error('预览失败', e)
  }
}
