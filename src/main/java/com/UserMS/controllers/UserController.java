package com.UserMS.controllers;

import com.UserMS.dtos.StandardResponseStructure;
import com.UserMS.dtos.UserRegistrationDto;
import com.UserMS.dtos.UserResponseDto;
import com.UserMS.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
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
}
