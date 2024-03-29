package com.library.web.sys_notice.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.library.utils.ResultUtils;
import com.library.utils.ResultVo;
import com.library.web.sys_notice.entity.NoticeParm;
import com.library.web.sys_notice.entity.SysNotice;
import com.library.web.sys_notice.service.SysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/notice")
public class SysNoticeController {
    @Autowired
    private SysNoticeService sysNoticeService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody SysNotice sysNotice){
        sysNotice.setCreateTime(new Date());
        boolean save = sysNoticeService.save(sysNotice);
        if(save){
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败!");
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody SysNotice sysNotice){
        boolean save = sysNoticeService.updateById(sysNotice);
        if(save){
            return ResultUtils.success("编辑成功");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @DeleteMapping("/{noticeId}")
    public ResultVo delete(@PathVariable("noticeId") Long noticeId){
        boolean save = sysNoticeService.removeById(noticeId);
        if(save){
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败!");
    }

    //列表
    @GetMapping("/list")
    public ResultVo getList(NoticeParm parm){
        IPage<SysNotice> list = sysNoticeService.getList(parm);
        return ResultUtils.success("查询成功",list);
    }
}
