package com.lav.dsite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordLoginDto {

    @Email
    @Size(max = 320)
    @NotBlank
    private String email;
    
    @Size(max = 255)
    @NotBlank
    private String password;
    
}
