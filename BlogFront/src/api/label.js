import request from '@/utils/request'
//添加标签
export const addLabelApi = (data) => request.post('/label', data)

//查询分页
export const queryPageApi = (name,status,date,page,pageSize) => 
  request.get(`/label?name=${name}&status=${status}&date=${date}&page=${page}&pageSize=${pageSize}`)

//根据id查询标签详情
export const getLabelByIdApi = (id) => request.get(`/label/${id}`)

//修改标签
export const updateLabelApi = (data,id) => request.put(`/label/${id}`, data)

//删除标签
export const deleteLabelApi = (id) => request.delete(`/label/${id}`)

//更新标签文章数
export const updateLabelTotalApi = () => request.put(`/label/total`)