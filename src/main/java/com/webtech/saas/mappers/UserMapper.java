package com.webtech.saas.mappers;

import com.webtech.saas.entities.User;
import com.webtech.saas.requests.UserRequest;
import com.webtech.saas.responses.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toEntity(UserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
