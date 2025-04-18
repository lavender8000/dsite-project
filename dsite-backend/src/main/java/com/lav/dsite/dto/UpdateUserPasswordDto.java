package com.lav.dsite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserPasswordDto {

    @Size(max = 255)
    private String currentPassword;

    @Size(max = 255)
    @NotBlank
    private String newPassword;

}
