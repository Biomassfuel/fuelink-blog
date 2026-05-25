import request from '@/utils/request'

//添加文章
export const addBlogApi = (data) => request.post('/blog', data)

//获取所有标签
export const getAllLabelApi = () => request.get('/label/all')

//上传文件
export const uploadFileApi = (data) => request.post('/blog/upload', data)

