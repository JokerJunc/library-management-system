package com.library.web.borrow_book.entity;

import lombok.Data;

@Data
public class ReturnParm {

    private Long borrowId;
    //图书id
    private Long bookId;
}
