import request from '@/utils/request'

// 查询分页
export const queryPageApi = (name,status,begin,end,page,pageSize) => 
  request.get(`/blog?name=${name}&status=${status}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)

//状态改变
export const updateBlogStatusApi = (id, status) => request.put(`/blog/status/${id}/${status}`)

//删除
export const deleteBlogApi = (id) => request.delete(`/blog/${id}`)
//批量删除
export const deleteBlogsApi = (ids) => request.delete(`/blog?id=${ids}`)

//id获取全部信息
export const queryBlogByIdApi = (id) => request.get(`/blog/${id}`)
//更新
export const updateBlogApi = (data) => request.put(`/blog`, data)

//修改置顶
export const changeTopApi = (id,isTop) => request.put(`/blog/isTop/${id}/${isTop}`)
