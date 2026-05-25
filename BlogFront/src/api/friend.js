import request from '@/utils/request'
//添加标签
export const addFriendApi = (data) => request.post('/friend', data)

//查询分页好友
export const queryPageApi = (page,pageSize) => 
  request.get(`/friend?page=${page}&pageSize=${pageSize}`)

//根据id查询好友详情
export const getFriendByIdApi = (id) => request.get(`/friend/${id}`)

//修改好友
export const updateFriendApi = (data,id) => request.put(`/friend/${id}`, data)

//删除好友
export const deleteFriendApi = (id) => request.delete(`/friend/${id}`)

