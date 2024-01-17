package com.library.web.sys_reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.web.sys_reader.entity.ReaderParm;
import com.library.web.sys_reader.entity.SysReader;
import com.library.web.sys_reader.mapper.SysReaderMapper;
import com.library.web.sys_reader.service.SysReaderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SysReaderServiceImpl extends ServiceImpl<SysReaderMapper, SysReader> implements SysReaderService {
    @Override
    public IPage<SysReader> getList(ReaderParm parm) {
        //构造查询条件
        QueryWrapper<SysReader> query = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(parm.getIdCard())) {
            query.lambda().like(SysReader::getIdCard, parm.getIdCard());
        }
        if (StringUtils.isNotEmpty(parm.getLearnName())) {
            query.lambda().like(SysReader::getLearnName, parm.getLearnName());
        }
        if (StringUtils.isNotEmpty(parm.getPhone())) {
            query.lambda().like(SysReader::getPhone, parm.getPhone());
        }
        if (StringUtils.isNotEmpty(parm.getUsername())) {
            query.lambda().like(SysReader::getUsername, parm.getUsername());
        }
        //构造分页对象
        IPage<SysReader> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        return this.baseMapper.selectPage(page,query);
    }
}