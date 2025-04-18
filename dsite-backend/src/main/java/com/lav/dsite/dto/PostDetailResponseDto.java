package com.lav.dsite.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDetailResponseDto {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private String userNickName;

    private Long forumId;
    
    private String forumName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
