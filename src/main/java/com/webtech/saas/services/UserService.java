package com.webtech.saas.services;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.requests.UserRequest;
import com.webtech.saas.responses.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(final UserRequest request);

    void updateUser(final String userId, UserRequest request);

    void deleteUser(final String userId);

    UserResponse getUserById(String userId);

    PageResponse<UserResponse> getAllUsers(final int page, final int size);

    void enableUser(final String userId);

    void disableUser(final String userId);
}
