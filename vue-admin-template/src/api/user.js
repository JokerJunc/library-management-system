import request from '@/utils/request'
import http from '@/utils/http'
export function login(data) {
  return request({
    url: '/vue-admin-template/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-admin-template/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
//获取用户列表
export const getListApi = async (params) =>{
  return await http.get('/api/user/list',params)
}

//新增
export const addUserApi = async (params) =>{
  return await http.post('/api/user',params)
}
//编辑
export const editUserApi = async (params) =>{
  return await http.put('/api/user',params)
}
//删除
export const deleteUserApi = async (params) =>{
  return await http.delete('/api/user',params)
}