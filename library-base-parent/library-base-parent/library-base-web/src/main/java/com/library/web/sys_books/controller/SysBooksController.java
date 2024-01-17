package com.library.web.sys_books.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.library.utils.ResultUtils;
import com.library.utils.ResultVo;
import com.library.web.sys_books.entity.ListParm;
import com.library.web.sys_books.entity.SysBooks;
import com.library.web.sys_books.service.SysBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class SysBooksController {
    @Autowired
    private SysBooksService sysBooksService;

    //新增
    //新增
    @PostMapping
    public ResultVo add(@RequestBody SysBooks books){
        boolean save = sysBooksService.save(books);
        if(save){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //新增
    @PutMapping
    public ResultVo edit(@RequestBody SysBooks books){
        boolean save = sysBooksService.updateById(books);
        if(save){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @DeleteMapping("/{bookId}")
    public ResultVo delete(@PathVariable("bookId") Long bookId){
        boolean remove = sysBooksService.removeById(bookId);
        if(remove){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //列表
    @GetMapping("/list")
    public ResultVo getList(ListParm parm){
        IPage<SysBooks> list = sysBooksService.getList(parm);
        return ResultUtils.success("查询成功",list);
    }

}
