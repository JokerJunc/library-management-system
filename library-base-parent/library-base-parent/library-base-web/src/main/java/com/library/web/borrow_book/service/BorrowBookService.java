package com.library.web.borrow_book.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.library.web.borrow_book.entity.*;

import java.util.List;

public interface BorrowBookService extends IService<BorrowBook> {
    void borrow(BorrowParm borrowParm);

    /**
     * 获取借阅列表
     *
     * @param parm 渲染参数
     * @return 借阅列表
     */
    IPage<ReturnBook> getBorrowList(ListParm parm);


    //还书
    void returnBook(List<ReturnParm> list);
    //异常还书
    void exceptionBook(ExceptionParm parm);
    //借阅查看
    IPage<LookBorrow> getLookBorrowList(LookParm parm);
}