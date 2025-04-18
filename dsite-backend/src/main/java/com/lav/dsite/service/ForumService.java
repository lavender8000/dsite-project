package com.lav.dsite.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lav.dsite.dto.ForumSummaryDto;
import com.lav.dsite.entity.Forum;

public interface ForumService {

    Forum findForumById(Long id);

    List<ForumSummaryDto> getForumSummaries(Pageable pageable);
    
}
