import request from '@/utils/request'

export const BlogQueryApi = (id) => 
  request.get(`/front/blog?id=${id}`)

// 浏览量增加
export const viewCountAddApi = (id) => 
  request.get(`/front/blog/viewCountAdd?id=${id}`)