package com.library.web.borrow_book.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.library.utils.ResultUtils;
import com.library.utils.ResultVo;
import com.library.web.borrow_book.entity.*;
import com.library.web.borrow_book.service.BorrowBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowBookController {
    @Autowired
    private BorrowBookService borrowBookService;

    //借书
    @PostMapping
    public ResultVo borrow(@RequestBody BorrowParm borrowParm){
        borrowBookService.borrow(borrowParm);
        return ResultUtils.success("借书成功!");
    }
    //借書列表
    @GetMapping("/returnBookList")
    public ResultVo getBorrowList(ListParm parm){
        IPage<ReturnBook> borrowList = borrowBookService.getBorrowList(parm);
        return ResultUtils.success("查询成功",borrowList);
    }
    //还书
    @PostMapping("/returnBooks")
    public ResultVo returnBooks(@RequestBody List<ReturnParm> list){
        borrowBookService.returnBook(list);
        return ResultUtils.success("还书成功!");
    }
    //异常还书
    @PostMapping("/exceptionBooks")
    public ResultVo exceptionBooks(@RequestBody ExceptionParm parm){
        borrowBookService.exceptionBook(parm);
        return ResultUtils.success("还书成功!");
    }
    //借阅查看
    @GetMapping("/getLookBorrowList")
    public ResultVo getLookBorrowList(LookParm parm){
        IPage<LookBorrow> borrowList = borrowBookService.getLookBorrowList(parm);
        return ResultUtils.success("查询成功",borrowList);
    }
}