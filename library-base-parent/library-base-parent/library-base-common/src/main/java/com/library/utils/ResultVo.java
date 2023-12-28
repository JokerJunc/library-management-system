package com.library.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data //自动生成GET和SET方法
@AllArgsConstructor
public class ResultVo<T> {
    private String msg;
    private int code;  // 0成功，1失败
    private T data;
}