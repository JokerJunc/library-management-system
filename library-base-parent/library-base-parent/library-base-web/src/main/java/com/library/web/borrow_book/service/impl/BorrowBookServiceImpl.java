package com.library.web.borrow_book.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.exception.BusinessException;
import com.library.exception_advice.BusinessExceptionEnum;
import com.library.web.borrow_book.entity.*;
import com.library.web.borrow_book.mapper.BorrowBookMapper;
import com.library.web.borrow_book.service.BorrowBookService;
import com.library.web.sys_books.entity.SysBooks;
import com.library.web.sys_books.service.SysBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class BorrowBookServiceImpl extends ServiceImpl<BorrowBookMapper, BorrowBook> implements BorrowBookService {
    @Autowired
    private SysBooksService sysBooksService;

    private Lock lock = new ReentrantLock();

    @Override
    @Transactional
    public void borrow(BorrowParm borrowParm) {
        lock.lock();
        try {
            //查询图书库存
            QueryWrapper<SysBooks> query = new QueryWrapper<>();
            query.lambda().in(SysBooks::getBookId, borrowParm.getBookIds());
            List<SysBooks> list = sysBooksService.list(query);
            List<SysBooks> collect = list.stream().filter(item -> item.getBookStore().longValue() < 1L).collect(Collectors.toList());
            //如果存在库存不足
            if (collect.size() > 0) {
                List<String> stringList = collect.stream().map(SysBooks::getBookName).collect(Collectors.toList());
                throw new BusinessException(BusinessExceptionEnum.NO_STOCK.getCode(),
                        stringList + BusinessExceptionEnum.NO_STOCK.getMessage());
            }
            //库存充足，减库存，插入借书明细
            List<Long> bookIds = borrowParm.getBookIds();
            for(int i=0;i<bookIds.size();i++){
                Long bookId = bookIds.get(i);
                //减库存
                int res = sysBooksService.subBook(bookId);
                //查询借书表数据
                if(res > 0){
                    BorrowBook borrowBook = new BorrowBook();
                    borrowBook.setBookId(bookId);
                    borrowBook.setReaderId(borrowParm.getReaderId());
                    borrowBook.setReturnTime(borrowParm.getReturnTime());
                    borrowBook.setApplyStatus("1");
                    borrowBook.setBorrowStatus("1");
                    borrowBook.setBorrowTime(new Date());
                    this.baseMapper.insert(borrowBook);
                }
            }
        } finally {
            lock.unlock();
        }

    }
    //借書列表
    @Override
    public IPage<ReturnBook> getBorrowList(ListParm parm) {
        //构造分页对象
        Page<ReturnBook> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        return this.baseMapper.getBorrowList(page,parm);
    }
    //还书
    @Override
    @Transactional
    public void returnBook(List<ReturnParm> list) {
        for (int i = 0; i < list.size(); i++) {
            //更新图书库存
            int res = sysBooksService.addBook(list.get(i).getBookId());
            if (res > 0) {
                //更新状态为还书
                BorrowBook borrowBook = new BorrowBook();
                borrowBook.setBorrowStatus("2");//已還
                borrowBook.setBorrowId(list.get(i).getBorrowId());
                this.baseMapper.updateById(borrowBook);
            }
        }
    }
    //異常還書
    @Override
    public void exceptionBook(ExceptionParm parm) {
        // 0: 异常、破损  1：丢失 ：不能还库存
        String type = parm.getType();
        if(type.equals("0")){
            //加库存
            int res = sysBooksService.addBook(parm.getBookId());
            if(res > 0){
                //变更借书状态
                BorrowBook borrowBook = new BorrowBook();
                borrowBook.setBorrowId(parm.getBorrowId());
                borrowBook.setBorrowStatus("2"); //已还
                borrowBook.setReturnStatus("2"); //异常还书
                borrowBook.setExcepionText(parm.getExcepionText());
                this.baseMapper.updateById(borrowBook);
            }
        }else{ //丢失
            //变更借书状态
            BorrowBook borrowBook = new BorrowBook();
            borrowBook.setBorrowId(parm.getBorrowId());
            borrowBook.setBorrowStatus("2"); //已还
            borrowBook.setReturnStatus("3"); //丢失
            borrowBook.setExcepionText(parm.getExcepionText());
            this.baseMapper.updateById(borrowBook);
        }
    }
    @Override
    public IPage<LookBorrow> getLookBorrowList(LookParm parm) {
        //构造分页对象
        Page<LookBorrow> page = new Page<>();
        page.setSize(parm.getPageSize());
        page.setCurrent(parm.getCurrentPage());
        return this.baseMapper.getLookBorrowList(page,parm);
    }
}

