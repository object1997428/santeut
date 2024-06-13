package com.santeut.hiking.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

//이건 쓸지말지 고민중
@Data
@AllArgsConstructor
public class PagingResponse<T> {
    private int status;
    private T content;
    private boolean isFirst;
    private boolean isLast;
    private int page;
    private int totalPage;
    private int size;
    private boolean sorted = false;
    private boolean asc = false;
    private boolean filtered = false;
}