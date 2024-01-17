package com.library.web.sys_books.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.web.sys_books.entity.ListParm;
import com.library.web.sys_books.entity.SysBooks;
import com.library.web.sys_books.mapper.SysBooksMapper;
import com.library.web.sys_books.service.SysBooksService;
import org.springframework.stereotype.Service;


@Service
public class SysBooksServiceImpl extends ServiceImpl<SysBooksMapper, SysBooks> implements SysBooksService {
    @Override
    public IPage<SysBooks> getList(ListParm listParm) {
        //构造分页对象
        Page<SysBooks> page = new  Page();
        page.setCurrent(listParm.getCurrentPage());
        page.setSize(listParm.getPageSize());
        return this.baseMapper.getList(page,listParm);
    }

    @Override
    public int subBook(Long BookId) {
        return this.baseMapper.subBook(BookId);
    }
    @Override
    public int addBook(Long bookId) {
        return this.baseMapper.addBook(bookId);
    }

}
