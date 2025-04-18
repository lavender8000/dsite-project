package com.lav.dsite.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostSummaryDto {

    private Long id;

    private String title;

    private Long userId;

    private String userNickName;

    private Long forumId;

    private String content;

    private LocalDateTime createdAt;

}
