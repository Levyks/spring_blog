package com.levyks.spring_blog.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import com.levyks.spring_blog.dtos.auth.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import com.levyks.spring_blog.security.details.UserDetailsImpl;
import com.levyks.spring_blog.security.jwt.JwtUtils;

import com.levyks.spring_blog.models.Role;
import com.levyks.spring_blog.models.User;
import com.levyks.spring_blog.repositories.RoleRepository;
import com.levyks.spring_blog.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        return new LoginResponseDTO(jwt, UserDTO.fromUser(user));

    }

    @PostMapping("/register")
    public RegisterResponseDTO register(@Valid @RequestBody RegisterRequestDTO registerDTO) {

            if(userRepository.existsByEmail(registerDTO.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }

            User user = new User();
            user.setEmail(registerDTO.getEmail());
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            user.setFirstName(registerDTO.getFirstName());
            user.setLastName(registerDTO.getLastName());

            Set<Role> roles = new HashSet<>();

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("User Role not set"));

            roles.add(userRole);

            user.setRoles(roles);

            userRepository.save(user);

            return new RegisterResponseDTO(UserDTO.fromUser(user));

    }

    @GetMapping("/whoami")
    @PreAuthorize("isAuthenticated()")
    public UserDTO whoami(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return UserDTO.fromUser(userDetails.getUser());

    }

}
