package com.library.web.sys_menu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.library.utils.ResultUtils;
import com.library.utils.ResultVo;
import com.library.web.sys_menu.entity.SysMenu;
import com.library.web.sys_menu.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
    @PutMapping
    public ResultVo editMenu(@RequestBody SysMenu menu){
        menu.setUpdateTime(new Date());
        boolean update = sysMenuService.updateById(menu);
        if(update){
            return ResultUtils.success("修改成功！");
        }
        return ResultUtils.error("修改失败！");
    }
    //删除
    @DeleteMapping("/{menuId}")
    public ResultVo deleteMenu(@PathVariable("menuId") Long menuId){
        //判断是否有下级，有下级，不能删除
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        query.lambda().eq(SysMenu::getParentId,menuId);
        List<SysMenu> list = sysMenuService.list(query);
        if(list.size() > 0){
            return ResultUtils.error("该菜单存在下级，不能删除!");
        }
        boolean save = sysMenuService.removeById(menuId);
        if(save){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }
    /*@GetMapping("/list")
    public ResultVo getMenu(@PathVariable Long menuId){
//        SysMenu menu = sysMenuService.getById(menuId);
        List<SysMenu> menu = sysMenuService.menuList();
        return ResultUtils.success("查询成功！",menu);
    }*/
    @GetMapping("/list")
    public ResultVo getList(){
        List<SysMenu> list = sysMenuService.menuList();
        return ResultUtils.success("查询成功",list);
    }
    @GetMapping("/parentList")
    public ResultVo getParentList(){
        List<SysMenu> list = sysMenuService.parentList();
        return ResultUtils.success("查询成功",list);
    }
}
