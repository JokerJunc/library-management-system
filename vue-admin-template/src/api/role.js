import http from '@/utils/http'
//获取列表
export const getListApi = async(parms)=>{
    return await http.get('/api/role/list',parms)
}
// 新增
export const addRoleApi = async(parms)=>{
    return await http.post('/api/role',parms)
}
// 编辑
export const editRoleApi = async(parms)=>{
    return await http.put('/api/role',parms)
}
//删除
export const deleteRoleApi = async(parms)=>{
    return await http.delete('/api/role',parms)
}