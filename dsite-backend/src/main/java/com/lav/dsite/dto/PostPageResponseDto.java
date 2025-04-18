package com.lav.dsite.dto;

import java.util.List;

import lombok.Data;

@Data
public class PostPageResponseDto<T> {

    // 內容
    private List<T> content;

    // 總頁數
    private int totalPages;

    // 總元素數量
    private long totalElements;

    // 每頁的大小
    private int size;

    // 當前頁碼 (從 0 開始)
    private int number;

    // 是否是第一頁
    private boolean isFirst;

    // 是否是最後一頁
    private boolean isLast;

}
