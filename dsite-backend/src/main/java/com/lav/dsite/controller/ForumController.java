package com.lav.dsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lav.dsite.common.Result;
import com.lav.dsite.service.ForumService;
import com.lav.dsite.utils.ResultHandler;
import com.lav.dsite.dto.ForumSummaryDto;

@Controller
public class ForumController {

    @Autowired
    private ForumService forumService;

    private static final List<String> FORUM_SUMMARY_SORT_FIELDS = List.of("id", "name", "createdAt");

    @GetMapping("/forums")
    @ResponseBody
    public ResponseEntity<Result<List<ForumSummaryDto>>> getForumSummaries
    (
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir
    ) {

        if (!FORUM_SUMMARY_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        List<ForumSummaryDto> forumSummaries = forumService.getForumSummaries(pageable);

        Result<List<ForumSummaryDto>> result = Result.success(forumSummaries);

        return ResultHandler.getResponseEntity(result);

    }
}
