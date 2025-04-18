package com.lav.dsite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lav.dsite.dto.ForumSummaryDto;
import com.lav.dsite.entity.Forum;
import com.lav.dsite.repository.ForumRepository;
import com.lav.dsite.service.ForumService;

@Service
public class ForumServiceImpl implements ForumService {

    @Autowired
    private ForumRepository forumRepository;

    @Override
    public Forum findForumById(Long id) {
        return forumRepository.findById(id).orElse(null);
    }

    @Override
    public List<ForumSummaryDto> getForumSummaries(Pageable pageable) {
        
        Page<Forum> forums = forumRepository.findAll(pageable);

        List<ForumSummaryDto> forumSummaryDtos = forums.getContent().stream().map(ForumSummaryDto::fromForum).toList();

        return forumSummaryDtos;

    }

}
