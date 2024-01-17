package com.library.web.sys_menu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.web.sys_menu.entity.MakeTree;
import com.library.web.sys_menu.entity.SysMenu;
import com.library.web.sys_menu.mapper.SysMenuMapper;
import com.library.web.sys_menu.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

//    @Override
//    public List<SysMenu> parent1List() {
//        //只需要查询父级菜单
//        String[] types = {"0", "1"};
//        //构造查询条件
//        QueryWrapper<SysMenu> query = new QueryWrapper<>();
//        query.lambda().in(SysMenu::getType, Arrays.asList(types)).orderByAsc(SysMenu::getOrderNum);
//        List<SysMenu> sysMenus = this.baseMapper.selectList(query);
//        //构造顶级菜单
//        SysMenu menu = new SysMenu();
//        menu.setMenuId(0L);
//        menu.setParentId(-1L);
//        menu.setTitle("顶级菜单");
//        sysMenus.add(menu);
//        //组装树
//        List<SysMenu> list = MakeTree.makeMenuTree(sysMenus, -1L);
//        return list;
//    }
    @Override
    public List<SysMenu> parentList() {
        String[] types = {"0","1"};
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        query.lambda().in(SysMenu::getType, Arrays.asList(types)).orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menuList = this.baseMapper.selectList(query);
        //组装顶级菜单，防止无数据的时候，没有菜单
        SysMenu menu = new SysMenu();
        menu.setMenuId(0L);
        menu.setParentId(-1L);
        menu.setTitle("顶级菜单");
        menuList.add(menu);
        List<SysMenu> list = MakeTree.makeMenuTree(menuList, -1L);
        return list;
    }
}