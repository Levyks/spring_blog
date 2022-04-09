package com.levyks.spring_blog.dtos.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RegisterResponseDTO {
    private final String message = "User registered successfully";
    private final UserDTO user;
}
