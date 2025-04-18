package com.lav.dsite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lav.dsite.dto.PostDetailResponseDto;
import com.lav.dsite.dto.PostSummaryDto;
import com.lav.dsite.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.lav.dsite.dto.PostDetailResponseDto(p.id, p.title, p.content, u.id, u.nickName, f.id, f.name, p.createdAt, p.updatedAt) " +
    "FROM Post p JOIN p.user u JOIN p.forum f WHERE p.id = :postId")
    PostDetailResponseDto findPostDetailById(@Param("postId") Long postId);
    
    @Query("SELECT new com.lav.dsite.dto.PostSummaryDto(p.id, p.title, u.id, u.nickName, p.forum.id, p.content, p.createdAt) " +
           "FROM Post p JOIN p.user u " +
           "WHERE (:forumId IS NULL OR p.forum.id = :forumId) " +
           "AND (:userId IS NULL OR p.user.id = :userId) ")
    Page<PostSummaryDto> findAllPostSummaries(Pageable pageable, 
                                               @Param("forumId") Long forumId,
                                               @Param("userId") Long userId);


}
