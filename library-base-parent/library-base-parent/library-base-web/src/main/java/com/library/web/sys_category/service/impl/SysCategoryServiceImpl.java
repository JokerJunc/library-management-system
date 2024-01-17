package com.library.web.sys_category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.web.sys_category.entity.ListCateParm;
import com.library.web.sys_category.entity.SysCategory;
import com.library.web.sys_category.mapper.SysCategoryMapper;
import com.library.web.sys_category.service.SysCategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements SysCategoryService {
    @Override
    public IPage<SysCategory> getList(ListCateParm parm) {
        //构造分页对象
        IPage<SysCategory> page = new Page<>();
        page.setSize(parm.getPageSize());
        page.setCurrent(parm.getCurrentPage());
        //查询条件
        QueryWrapper<SysCategory> query = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(parm.getCategoryName())) {
            query.lambda().like(SysCategory::getCategoryName,parm.getCategoryName());
        }
        return this.baseMapper.selectPage(page,query);
    }
}