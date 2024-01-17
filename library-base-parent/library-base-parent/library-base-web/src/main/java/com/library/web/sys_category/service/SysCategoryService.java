package com.library.web.sys_category.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.library.web.sys_category.entity.ListCateParm;
import com.library.web.sys_category.entity.SysCategory;
public interface SysCategoryService extends IService<SysCategory> {
    IPage<SysCategory> getList(ListCateParm parm);
}


