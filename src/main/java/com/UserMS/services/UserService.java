package com.UserMS.services;

import com.UserMS.dtos.*;
import com.UserMS.entities.User;
import com.UserMS.exceptions.UserException;
import com.UserMS.repos.UserRepository;
import com.UserMS.util.JwtUtil;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AccessTokenStore accessTokenStore;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new UserException(
                "User with email " + registrationDto.getEmail() + " already exists",
                "USER_EXISTS"
            );
        }

        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setCity(registrationDto.getCity());
        user.setExperience(registrationDto.getExperience());
        user.setPhone(registrationDto.getPhone());
        user.setProfileSummary(registrationDto.getProfileSummary());
        user.setSkills(registrationDto.getSkills());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        return convertToResponseDto(savedUser);
    }
    
    public LoginResponseDto loginUser(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserException(
                    "Invalid credentials",
                    "INVALID_CREDENTIALS"
                ));
        
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UserException(
                "Invalid credentials",
                "INVALID_CREDENTIALS"
            );
        }
        
        if (!user.getIsActive()) {
            throw new UserException(
                "Account is deactivated",
                "ACCOUNT_DEACTIVATED"
            );
        }
        
        String token = jwtUtil.generateToken(user.getEmail());
        String jti = jwtUtil.getJtiFromToken(token);
        Date exp = jwtUtil.getExpirationFromToken(token);
        long ttlMs = Math.max(0, exp.getTime() - System.currentTimeMillis());
        accessTokenStore.store(jti, user.getEmail(), Duration.ofMillis(ttlMs));
        return new LoginResponseDto(token, jwtExpiration / 1000); // Convert to seconds
    }

    public void logout(String bearerTokenOrJwt) {
        if (bearerTokenOrJwt == null || bearerTokenOrJwt.isBlank()) return;
        String token = bearerTokenOrJwt.startsWith("Bearer ")
                ? bearerTokenOrJwt.substring(7)
                : bearerTokenOrJwt;
        if (!jwtUtil.validateToken(token)) return;
        String jti = jwtUtil.getJtiFromToken(token);
        accessTokenStore.revoke(jti);
    }

    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(
                    "User not found with email: " + email,
                    "USER_NOT_FOUND"
                ));
        return convertToResponseDto(user);
    }

    private UserResponseDto convertToResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUserId(user.getUserId());
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setEmail(user.getEmail());
        responseDto.setCity(user.getCity());
        responseDto.setExperience(user.getExperience());
        responseDto.setPhone(user.getPhone());
        responseDto.setProfileSummary(user.getProfileSummary());
        responseDto.setSkills(user.getSkills());
        responseDto.setIsActive(user.getIsActive());
        responseDto.setCreatedAt(user.getCreatedAt());
        responseDto.setUpdatedAt(user.getUpdatedAt());
        return responseDto;
    }
}