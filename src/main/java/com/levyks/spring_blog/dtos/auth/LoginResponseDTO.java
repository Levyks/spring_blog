package com.levyks.spring_blog.dtos.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponseDTO {
    private final String message = "Logged in successfully";
    private final String token;
    private final UserDTO user;
}
