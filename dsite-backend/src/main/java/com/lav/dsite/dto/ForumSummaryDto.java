package com.lav.dsite.dto;

import java.time.LocalDateTime;

import com.lav.dsite.entity.Forum;

import lombok.Data;

@Data
public class ForumSummaryDto {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    public static ForumSummaryDto fromForum(Forum forum) {
        ForumSummaryDto forumSummaryDto = new ForumSummaryDto();
        forumSummaryDto.setId(forum.getId());
        forumSummaryDto.setName(forum.getName());
        forumSummaryDto.setCreatedAt(forum.getCreatedAt());
        return forumSummaryDto;
    }

}
