package com.library.web.sys_menu.entity;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MakeTree {

    public static List<SysMenu> makeMenuTree(List<SysMenu> menuList, Long pid) {
        List<SysMenu> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && item.getParentId() == pid)
                .forEach(dom -> {
                    SysMenu menu = new SysMenu();
                    BeanUtils.copyProperties(dom, menu);
                    //查询该项的下级菜单
                    List<SysMenu> sysMenus = makeMenuTree(menuList, dom.getMenuId());
                    menu.setChildren(sysMenus);
                    list.add(menu);
                });
        return list;
    }
}