package com.lav.dsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lav.dsite.entity.Forum;

public interface ForumRepository extends JpaRepository<Forum, Long> {

}
