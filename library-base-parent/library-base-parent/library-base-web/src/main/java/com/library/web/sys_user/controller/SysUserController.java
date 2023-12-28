package com.library.web.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.library.utils.ResultUtils;
import com.library.utils.ResultVo;
import com.library.web.sys_user.entity.PageParm;
import com.library.web.sys_user.entity.SysUser;
import com.library.web.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping
    public ResultVo addUser(@RequestBody SysUser user) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.lambda().eq(SysUser::getUsername, user.getUsername());
        SysUser one = sysUserService.getOne(query);
        if (one != null) {
            return ResultUtils.error("用户名已存在");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //设置是不是管理员
        user.setIsAdmin("0");
        user.setCreateTime(new Date());
        //入库
        boolean save = sysUserService.save(user);
        if (save) {
            return ResultUtils.success("新增用户成功");
        }
        return ResultUtils.error("新增用户失败");

    }
    //编辑用户接口
    @PutMapping
    public ResultVo editUser(@RequestBody SysUser user) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.lambda().eq(SysUser::getUsername, user.getUsername());
        SysUser one = sysUserService.getOne(query);
        if (one != null && one.getUserId() != user.getUserId()) {
            return ResultUtils.error("用户名已存在");
        }
        if(StringUtils.isNotEmpty(user.getPassword())) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        //入库
        boolean save = sysUserService.updateById(user);
        if (save) {
            return ResultUtils.success("编辑用户成功!");
        }
        return ResultUtils.error("编辑用户失败");

    }
    @DeleteMapping("/{userId}")
    public ResultVo deleteUser(@PathVariable("userId") Long userId) {
        boolean remove = sysUserService.removeById(userId);
        if (remove) {
            return ResultUtils.success("删除用户成功!");
        } else {
            return ResultUtils.error("删除用户失败!");
        }
    }
    //列表查询
    @GetMapping("/list")

    public ResultVo getList(PageParm parm) {
        IPage<SysUser> list = sysUserService.list(parm);
        // 对list中的每个记录进行操作
        list.getRecords().stream().forEach(item -> {
            // 设置记录的密码为null
            item.setPassword(null);
        });

        return ResultUtils.success("查询成功", list);
    }
}
