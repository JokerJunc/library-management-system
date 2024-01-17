package com.library.web.borrow_book.entity;

import lombok.Data;

@Data
public class ListParm {
    private Long currentPage;
    private Long pageSize;
    private String username;
    private String borrowStatus;
}