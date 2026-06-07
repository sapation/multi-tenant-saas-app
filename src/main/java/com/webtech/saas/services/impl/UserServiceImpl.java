package com.webtech.saas.services.impl;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.repositories.UserRepository;
import com.webtech.saas.requests.UserRequest;
import com.webtech.saas.responses.UserResponse;
import com.webtech.saas.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public void createUser(UserRequest request) {

    }

    @Override
    public void updateUser(String userId, UserRequest request) {

    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public UserResponse getUserById(String userId) {
        return null;
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        return null;
    }

    @Override
    public void enableUser(String userId) {

    }

    @Override
    public void disableUser(String userId) {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User with these username: " + username + "does not exist"));
    }
}
