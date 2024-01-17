package com.library.web.sys_books.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.web.sys_books.entity.ListParm;
import com.library.web.sys_books.entity.SysBooks;
import org.apache.ibatis.annotations.Param;

public interface SysBooksMapper extends BaseMapper<SysBooks> {
    IPage<SysBooks> getList(Page<SysBooks> page, @Param("parm") ListParm parm);
    int subBook(@Param("BookId") Long BookId);

    //还书加库存
    int addBook(@Param("bookId") Long bookId);
}
