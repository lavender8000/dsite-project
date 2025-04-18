package com.lav.dsite.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lav.dsite.dto.PostPageResponseDto;
import com.lav.dsite.dto.PostSearchDto;
import com.lav.dsite.dto.PostCreateDto;
import com.lav.dsite.dto.PostDetailResponseDto;
import com.lav.dsite.dto.PostSummaryDto;
import com.lav.dsite.dto.PostUpdateDto;
import com.lav.dsite.dto.UserRedisDto;
import com.lav.dsite.entity.Forum;
import com.lav.dsite.entity.Post;
import com.lav.dsite.entity.User;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.exception.PostServiceException;
import com.lav.dsite.repository.PostRepository;
import com.lav.dsite.service.PostService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PostServiceImpl implements PostService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Post savePost(Post post) {
        try {

            return postRepository.save(post);

        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();

            boolean userError = message.matches("(?i).*user_id.*");
            boolean forumError = message.matches("(?i).*forum_id.*");

            if (forumError) {
                throw new PostServiceException(ResponseStatus.FORUM_NOT_FOUND, e);
            }

            if (userError) {
                throw new PostServiceException(ResponseStatus.USER_NOT_FOUND, e);
            }
            
            throw new PostServiceException(ResponseStatus.POST_SAVE_FAILED, e);
        } catch (OptimisticLockingFailureException e) {
            throw new PostServiceException(ResponseStatus.OPTIMISTIC_LOCK_EXCEPTION, e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Post createPost(PostCreateDto postCreateDto) {

        UserRedisDto userRedisDto = (UserRedisDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = entityManager.getReference(User.class, userRedisDto.getId());
        Forum forum = entityManager.getReference(Forum.class, postCreateDto.getForumId());

        Post newPost = new Post();

        newPost.setUser(user);
        newPost.setForum(forum);
        newPost.setTitle(postCreateDto.getTitle().trim());
        newPost.setContent(postCreateDto.getContent());
        
        return savePost(newPost);

    }

    @Override
    public PostDetailResponseDto getPostDetail(Long postId) {

        PostDetailResponseDto postDetailResponseDto = postRepository.findPostDetailById(postId);

        if (postDetailResponseDto == null) {
            // 文章不存在
            throw new PostServiceException(ResponseStatus.POST_NOT_FOUND);
        }

        return postDetailResponseDto;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Post updatePost(Long postId, PostUpdateDto postUpdateDto) {

        Post loadedPost = postRepository.findById(postId).orElse(null);

        if (loadedPost == null) {
            // 文章不存在
            throw new PostServiceException(ResponseStatus.POST_NOT_FOUND);
        }

        Long postUserId = loadedPost.getUser().getId();

        UserRedisDto userRedisDto = (UserRedisDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userRedisDto.getId();

        if (userId != postUserId) {
            // 不是作者
            throw new PostServiceException(ResponseStatus.FORBIDDEN);
        }

        Forum forum = entityManager.getReference(Forum.class, postUpdateDto.getForumId());
        User user = loadedPost.getUser();

        loadedPost.setUser(user);
        loadedPost.setForum(forum);
        loadedPost.setTitle(postUpdateDto.getTitle().trim());
        loadedPost.setContent(postUpdateDto.getContent());
        loadedPost.setUpdatedAt(LocalDateTime.now());

        return savePost(loadedPost);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePost(Long postId) {
        
        Post loadedPost = postRepository.findById(postId).orElse(null);
        
        if (loadedPost == null) {
            // 文章不存在
            throw new PostServiceException(ResponseStatus.POST_NOT_FOUND);
        }

        Long postUserId = loadedPost.getUser().getId();

        UserRedisDto userRedisDto = (UserRedisDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userRedisDto.getId();

        if (userId != postUserId) {
            // 不是作者
            throw new PostServiceException(ResponseStatus.FORBIDDEN);
        }

        postRepository.deleteById(postId);

    }

    @Override
    public Page<PostSummaryDto> getPostSummaries(Pageable pageable, PostSearchDto postSearchDto) {

        Page<PostSummaryDto> postSummaries = postRepository.findAllPostSummaries(pageable, postSearchDto.getForumId(), postSearchDto.getUserId());

        return postSummaries;

    }

    @Override
    public PostPageResponseDto<PostSummaryDto> getPostPageSummaries(Pageable pageable, PostSearchDto postSearchDto) {
        
        Page<PostSummaryDto> postSummaries = getPostSummaries(pageable, postSearchDto);

        PostPageResponseDto<PostSummaryDto> postPageResponseDto = new PostPageResponseDto<>();
        postPageResponseDto.setContent(postSummaries.getContent());
        postPageResponseDto.setTotalPages(postSummaries.getTotalPages());
        postPageResponseDto.setTotalElements(postSummaries.getTotalElements());
        postPageResponseDto.setSize(postSummaries.getSize());
        postPageResponseDto.setNumber(postSummaries.getNumber());
        postPageResponseDto.setFirst(postSummaries.isFirst());
        postPageResponseDto.setLast(postSummaries.isLast());

        return postPageResponseDto;

    }

}
