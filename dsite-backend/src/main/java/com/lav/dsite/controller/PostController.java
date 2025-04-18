package com.lav.dsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lav.dsite.common.Result;
import com.lav.dsite.dto.PostPageResponseDto;
import com.lav.dsite.dto.PostCreateDto;
import com.lav.dsite.dto.PostDetailResponseDto;
import com.lav.dsite.dto.PostResponseDto;
import com.lav.dsite.dto.PostSearchDto;
import com.lav.dsite.dto.PostSummaryDto;
import com.lav.dsite.dto.PostUpdateDto;
import com.lav.dsite.entity.Post;
import com.lav.dsite.service.PostService;
import com.lav.dsite.utils.ResultHandler;

import jakarta.validation.Valid;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    private static final List<String> POST_SUMMARY_SORT_FIELDS = List.of("id", "userNickName", "title", "createdAt");

    @PostMapping("/posts")
    @ResponseBody
    public ResponseEntity<Result<PostResponseDto>> createPost(@RequestBody @Valid PostCreateDto postCreateDTO) {

        Post createdPost = postService.createPost(postCreateDTO);

        Result<PostResponseDto> result = Result.created(PostResponseDto.fromPost(createdPost));

        return ResultHandler.getResponseEntity(result);
        
    }

    @GetMapping("/posts/{postId}")
    @ResponseBody
    public ResponseEntity<Result<PostDetailResponseDto>> getPost(@PathVariable Long postId) {

        PostDetailResponseDto postDetailDto = postService.getPostDetail(postId);

        Result<PostDetailResponseDto> result = Result.success(postDetailDto);

        return ResultHandler.getResponseEntity(result);

    }

    @PutMapping("/posts/{postId}")
    @ResponseBody
    public ResponseEntity<Result<PostResponseDto>> updatePost(@PathVariable Long postId, @RequestBody @Valid PostUpdateDto postUpdateDto) {

        Post updatedPost = postService.updatePost(postId, postUpdateDto);

        Result<PostResponseDto> result = Result.success(PostResponseDto.fromPost(updatedPost));

        return ResultHandler.getResponseEntity(result);

    }

    @DeleteMapping("/posts/{postId}")
    @ResponseBody
    public ResponseEntity<Result<Void>> deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);

        Result<Void> result = Result.success();

        return ResultHandler.getResponseEntity(result);

    }

    @GetMapping("/posts")
    @ResponseBody
    public ResponseEntity<Result<PostPageResponseDto<PostSummaryDto>>> getPostSummaries
    (
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
        @Valid PostSearchDto postSearchDto
    ) {

        if (!POST_SUMMARY_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PostPageResponseDto<PostSummaryDto> postPageResponseDto = postService.getPostPageSummaries(pageable, postSearchDto);

        Result<PostPageResponseDto<PostSummaryDto>> result = Result.success(postPageResponseDto);

        return ResultHandler.getResponseEntity(result);

    }

}
