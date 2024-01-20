import http from '@/utils/http'
//列表
export const getListApi = async(parm) =>{
    return await http.get('/api/reader/list',parm)
}
//新增
export const addReaderApi = async(parm) =>{
    return await http.post("/api/reader",parm)
}
//编辑
export const editReaderApi = async(parm) =>{
    return await http.put("/api/reader",parm)
}
//删除
export const deleteReaderApi = async(parm) =>{
    return await http.delete("/api/reader",parm)
}
//学号查询
export const getByUserNameApi = async(parm) =>{
    return await http.get("/api/reader/getByUserName",parm)
}