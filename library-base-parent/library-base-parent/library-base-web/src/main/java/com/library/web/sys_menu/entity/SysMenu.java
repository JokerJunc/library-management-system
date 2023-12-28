package com.library.web.sys_menu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@TableName("sys_menu")
public class SysMenu {
    @TableId(type = IdType.AUTO)
    private Long menuId;//菜单Id
    private Long parentId;//父级菜单Id
    private String title;//菜单名称
    private String code;//权限字段
    private String name;//路由name
    private String path;//路由path
    private String url;//菜单url
    private String type;//菜单类型 (0 目录 1菜单，2按钮)
    private String icon;//菜单图标
    private String parentName;//父级菜单名称
    private Long orderNum;//序号
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    //该字段不属于数据库表，需要排除
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}
