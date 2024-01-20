import http from '@/utils/http'
export const getMenuListApi = async() =>{
    return await http.get("/api/menu/list",null)
}
export const getParentMenuListApi = async() =>{
    return await http.get("/api/menu/parentList",null)
}
//新增
export const addMenuApi = async(data) =>{
    return await http.post("/api/menu/",data)
}
//编辑
export const editMenuApi = async(data) =>{
    return await http.put("/api/menu/",data)
}
export const deleteMenuApi = async(id) =>{
    return await http.delete("/api/menu/",id)
}