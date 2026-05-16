package com.UserMS.controllers;

import com.UserMS.dtos.*;
import com.UserMS.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<StandardResponseStructure> registerUser(
            @Valid @RequestBody UserRegistrationDto registrationDto) {
        
        UserResponseDto createdUser = userService.registerUser(registrationDto);
        StandardResponseStructure response = StandardResponseStructure.success(createdUser);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(
            @Valid @RequestBody LoginDto loginDto) {
        
        LoginResponseDto response = userService.loginUser(loginDto);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        userService.logout(authHeader);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getProfile(
            @RequestHeader("X-User-Name") String username) {
        // Gateway adds X-User-Name header after JWT validation
        UserResponseDto user = userService.getUserByEmail(username);
        return ResponseEntity.ok(user);
    }
}
