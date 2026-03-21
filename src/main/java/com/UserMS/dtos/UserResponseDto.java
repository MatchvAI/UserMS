package com.UserMS.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private Integer experience;
    private String phone;
    private String profileSummary;
    private String skills;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}