package com.library.web.sys_menu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.web.sys_menu.entity.MakeTree;
import com.library.web.sys_menu.entity.SysMenu;
import com.library.web.sys_menu.mapper.SysMenuMapper;
import com.library.web.sys_menu.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> menuList() {
        //查询列表
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        query.lambda().orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menuList = this.baseMapper.selectList(query);
        //组装树
        List<SysMenu> list = MakeTree.makeMenuTree(menuList, 0L);
        return list;
    }
}