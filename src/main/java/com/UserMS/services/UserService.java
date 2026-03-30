package com.UserMS.services;

import com.UserMS.dtos.UserRegistrationDto;
import com.UserMS.dtos.UserResponseDto;
import com.UserMS.entities.User;
import com.UserMS.exceptions.UserException;
import com.UserMS.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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