package com.lav.dsite.dto;

import java.time.LocalDateTime;

import com.lav.dsite.entity.Post;

import lombok.Data;

@Data
public class PostResponseDto {

    private Long id;

    private Long userId;

    private Long forumId;

    private String title;

    private String content;

    private int count;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    public static PostResponseDto fromPost(Post post) {
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setUserId(post.getUser().getId());
        postResponseDto.setForumId(post.getForum().getId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setContent(post.getContent());
        postResponseDto.setCount(post.getCount());
        postResponseDto.setCreatedAt(post.getCreatedAt());
        postResponseDto.setUpdatedAt(post.getUpdatedAt());
        return postResponseDto;
    }
}
