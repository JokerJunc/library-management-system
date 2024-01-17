package com.library.web.sys_books.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.library.web.sys_books.entity.ListParm;
import com.library.web.sys_books.entity.SysBooks;

public interface SysBooksService extends IService<SysBooks> {
    IPage<SysBooks> getList(ListParm listParm);
    int subBook(Long BookId);

    //还书加库存
    int addBook(Long bookId);
}
