package com.library.web.sys_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.library.web.sys_menu.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> menuList();
}
