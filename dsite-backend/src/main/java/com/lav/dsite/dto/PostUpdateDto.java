package com.lav.dsite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostUpdateDto {

    @NotNull
    private Long forumId;

    @Size(max = 80)
    @NotBlank
    private String title;

    @NotBlank
    private String content;
    
}
