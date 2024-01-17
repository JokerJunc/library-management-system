package com.library.web.sys_reader.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_reader")
public class SysReader {
    @TableId(type = IdType.AUTO)
    private Long readerId;
    private String learnName;//姓名
    private String username;//学号
    private String idCard;
    private String sex;
    private String phone;
    private String password;
    private String type;
    private String checkStatus;
    private String userStatus;
    private String className;
}