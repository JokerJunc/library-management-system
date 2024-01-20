import http from '@/utils/http'
// 获取分类列表
export const getCategoryListApi = async(params) => {
  return await http.get('/api/category/list', params)
}
//新增
export const addCategoryApi = async(data) => {
  return await http.post('/api/category/', data)
}
//编辑
export const editCategoryApi = async(data) => {
  return await http.put('/api/category/', data)
}
//删除
export const deleteCategoryApi = async(params) => {
  return await http.delete('/api/category' , params)
}