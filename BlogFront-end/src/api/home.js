import request from '@/utils/request'
//查询标签
export const labelQueryAllApi = () => request.get(`/allLabel`)

//查询所有博客
export const allBlogQueryApi = (titleSearch,tagId,currentPage,pageSize) => request.get(`/allBlog?titleSearch=${titleSearch}&tagId=${tagId}&page=${currentPage}&pageSize=${pageSize}`)

//查询所有友情链接
export const queryPageApi = () => request.get(`/friend/all`)