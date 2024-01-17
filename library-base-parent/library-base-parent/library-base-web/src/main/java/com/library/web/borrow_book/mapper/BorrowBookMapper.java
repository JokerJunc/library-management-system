package com.library.web.borrow_book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.web.borrow_book.entity.*;
import org.apache.ibatis.annotations.Param;

public interface BorrowBookMapper extends BaseMapper<BorrowBook> {
    IPage<ReturnBook> getBorrowList(Page<ReturnBook> page, @Param("parm") ListParm parm);
    //借阅查看
    IPage<LookBorrow> getLookBorrowList(Page<LookBorrow> page, @Param("parm") LookParm parm);
}