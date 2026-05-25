import axios from 'axios'

//创建axios实例对象
const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

request.interceptors.response.use(
  (response) => { //成功回调
    return response.data
  }
)

export default request