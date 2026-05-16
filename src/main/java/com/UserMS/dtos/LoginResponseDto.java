package com.UserMS.dtos;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    
    public LoginResponseDto(String accessToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}
