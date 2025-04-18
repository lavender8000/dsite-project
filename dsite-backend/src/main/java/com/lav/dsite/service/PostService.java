package com.lav.dsite.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lav.dsite.dto.PostCreateDto;
import com.lav.dsite.dto.PostDetailResponseDto;
import com.lav.dsite.dto.PostPageResponseDto;
import com.lav.dsite.dto.PostSearchDto;
import com.lav.dsite.dto.PostSummaryDto;
import com.lav.dsite.dto.PostUpdateDto;
import com.lav.dsite.entity.Post;

public interface PostService {

    Post savePost(Post post);

    Post createPost(PostCreateDto postCreateDtO);

    PostDetailResponseDto getPostDetail(Long postId);

    Post updatePost(Long postId, PostUpdateDto postUpdateDto);

    void deletePost(Long postId);

    Page<PostSummaryDto> getPostSummaries(Pageable pageable, PostSearchDto postSearchDto);

    PostPageResponseDto<PostSummaryDto> getPostPageSummaries(Pageable pageable, PostSearchDto postSearchDto);

}
