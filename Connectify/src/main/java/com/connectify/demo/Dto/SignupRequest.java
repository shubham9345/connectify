package com.connectify.demo.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    private String name;

    private String userBio;

    @NotBlank
    private String username;

    private String url;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
    private String roles;
}