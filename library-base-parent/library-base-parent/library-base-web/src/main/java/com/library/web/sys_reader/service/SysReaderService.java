package com.library.web.sys_reader.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.library.web.sys_reader.entity.ReaderParm;
import com.library.web.sys_reader.entity.SysReader;

public interface SysReaderService extends IService<SysReader> {
    IPage<SysReader> getList(ReaderParm parm);
}
