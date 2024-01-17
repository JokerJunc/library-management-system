package com.library.web.sys_category.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.library.utils.ResultUtils;
import com.library.utils.ResultVo;
import com.library.web.sys_category.entity.ListCateParm;
import com.library.web.sys_category.entity.SysCategory;
import com.library.web.sys_category.service.SysCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class SysCategoryController {
                                                                                            @Autowired
    private SysCategoryService sysCategoryService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody SysCategory category){
        boolean save = sysCategoryService.save(category);
        if(save){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //新增
    @PutMapping
    public ResultVo edit(@RequestBody SysCategory category){
        boolean save = sysCategoryService.updateById(category);
        if(save){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @DeleteMapping("/{categoryId}")
    public ResultVo delete(@PathVariable("categoryId") Long categoryId){
        boolean remove = sysCategoryService.removeById(categoryId);
        if(remove){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //列表
    @GetMapping("/list")
    public ResultVo getList(ListCateParm parm){
        IPage<SysCategory> list = sysCategoryService.getList(parm);
        return ResultUtils.success("查询成功",list);
    }
    //图书列表
    @GetMapping("/cateList")
    public ResultVo getCateList(ListCateParm parm){
        List<SysCategory> list = sysCategoryService.list();
        return ResultUtils.success("查询成功",list);
    }
}
