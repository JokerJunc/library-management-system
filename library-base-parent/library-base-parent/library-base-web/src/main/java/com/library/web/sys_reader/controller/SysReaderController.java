package com.library.web.sys_reader.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.library.utils.ResultUtils;
import com.library.utils.ResultVo;
import com.library.web.sys_reader.entity.ReaderParm;
import com.library.web.sys_reader.entity.SysReader;
import com.library.web.sys_reader.service.SysReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reader")
public class SysReaderController {
    @Autowired
    private SysReaderService sysReaderService;

    //新增
    @PostMapping
    public ResultVo addReader(@RequestBody SysReader reader){
//        查询学号是否被占用
        QueryWrapper<SysReader> query = new QueryWrapper<>();
        query.lambda().eq(SysReader::getUsername, reader.getUsername());
        if(sysReaderService.getOne(query) != null){
            return ResultUtils.error("学号被占用！");
        }
        reader.setCheckStatus("1");
        reader.setUserStatus("1");
        boolean save = sysReaderService.save(reader);
        if(save){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败！");
    }

    //编辑
    @PutMapping
    public ResultVo editReader(@RequestBody SysReader reader){
        boolean save = sysReaderService.updateById(reader);
        if(save){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败！");
    }

    //删除
    @DeleteMapping("/{readerId}")
    public ResultVo deleteReader(@PathVariable("readerId") Long readerId){
        boolean remove = sysReaderService.removeById(readerId);
        if(remove){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败！");
    }

    //列表
    @GetMapping("/list")
    public ResultVo getList(ReaderParm parm){
        IPage<SysReader> list = sysReaderService.getList(parm);
        return ResultUtils.success("查询成功",list);
    }
    //根据学号查询
    @GetMapping("/getByUserName")
    public ResultVo getReader(SysReader reader){
        QueryWrapper<SysReader> query = new QueryWrapper<>();
        query.lambda().eq(SysReader::getUsername,reader.getUsername());
        SysReader one = sysReaderService.getOne(query);
        return ResultUtils.success("查询成功",one);
    }
}