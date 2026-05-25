import request from '@/utils/request'

export const archiveApi = () => 
  request.get(`/archive`)