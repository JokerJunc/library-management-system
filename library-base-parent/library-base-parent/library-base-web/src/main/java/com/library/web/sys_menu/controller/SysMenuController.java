package com.library.web.sys_menu.controller;

import com.library.utils.ResultUtils;
import com.library.utils.ResultVo;
import com.library.web.sys_menu.entity.SysMenu;
import com.library.web.sys_menu.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping
    public ResultVo addMenu(@RequestBody SysMenu menu){
        menu.setCreateTime(new Date());
        boolean save = sysMenuService.save(menu);
        if(save){
            return ResultUtils.success("添加成功！");
        }
        return ResultUtils.error("添加失败！");
    }
}
