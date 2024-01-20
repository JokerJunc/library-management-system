import http from "@/utils/http";
//借书
export const borrowBookApi = async (parm) => {
  return http.post("/api/borrow", parm);
};
//还书列表
export const returnBorrowApi = async(parm) =>{
  return http.get("/api/borrow/returnBookList",parm)
}
//还书
export const returnBooksApi = async (parm) => {
  return http.post("/api/borrow/returnBooks", parm);
};
//异常还书
export const exceptionBooksApi = async(parm) =>{
  return await http.post("/api/borrow/exceptionBooks",parm)
}
//借阅列表
export const getLookBorrowListApi = async(parm) =>{
  return await http.get("/api/borrow/getLookBorrowList",parm)
}
