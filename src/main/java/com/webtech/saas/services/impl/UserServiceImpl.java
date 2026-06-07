package com.webtech.saas.services.impl;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.config.TenantContext;
import com.webtech.saas.entities.Tenant;
import com.webtech.saas.entities.User;
import com.webtech.saas.entities.UserRole;
import com.webtech.saas.exceptions.InvalidRequestException;
import com.webtech.saas.mappers.UserMapper;
import com.webtech.saas.repositories.UserRepository;
import com.webtech.saas.requests.UserRequest;
import com.webtech.saas.responses.UserResponse;
import com.webtech.saas.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserRequest request) {
        final String tenantId = TenantContext.getCurrentTenant();

        // check if username already exist
        if (this.repository.existsByUsername(request.getUsername())) {
            throw new DuplicateKeyException("Username already exist");
        }

        // check if email already exist
        if (this.repository.existsByEmail(request.getEmail())) {
            throw new DuplicateKeyException("Email already exist");
        }

        // validate role (it should not be platform_admin)
        if (request.getRole() == UserRole.ROLE_PLATFORM_ADMIN) {
            throw new InvalidRequestException("Platform admin cannot be selected as a role");
        }

        final User user = userMapper.toEntity(request);
        user.setTenant(Tenant.builder().Id(tenantId).build());
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));

        this.repository.save(user);
        log.info("User created successfully");
    }

    @Override
    public void updateUser(String userId, UserRequest request) {
        final String tenantId = TenantContext.getCurrentTenant();
        log.info("Updating user for tenant: {}", tenantId);

        final User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(()-> new EntityNotFoundException("User does not exist"));

        // check if user belongs to tenant
        if (!user.getTenantId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        // check if username being change and if it is already taken
        if (!user.getUsername().equals(request.getUsername()) && this.repository.existsByUsername(request.getUsername())) {
            throw new DuplicateKeyException("Username already exist");
        }

        // check if email being change and already taken
        if (!user.getEmail().equals(request.getEmail()) && this.repository.existsByEmail(request.getEmail())) {
            throw new DuplicateKeyException("Email already exist");
        }

        // validate role (it should not be platform_admin)
        if (request.getRole() == UserRole.ROLE_PLATFORM_ADMIN) {
            throw new InvalidRequestException("Platform admin cannot be selected as a role");
        }

        // Update user details
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());

        this.repository.save(user);
        log.info("User Updated successfully");
    }

    @Override
    public void deleteUser(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        log.info("Deleting user for tenant: {}", tenantId);

        final User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(()-> new EntityNotFoundException("User does not exist"));

        // check if user belongs to tenant
        if (!user.getTenantId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        // Soft delete
        user.setDeleted(true);
        this.repository.save(user);
        log.info("User Deleted Successfully");
    }

    @Override
    public UserResponse getUserById(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();

        final User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(()-> new EntityNotFoundException("User does not exist"));

        // check if user belongs to tenant
        if (!user.getTenantId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        return this.userMapper.toResponse(user);
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        final String tenantId = TenantContext.getCurrentTenant();
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<User> userPage = this.repository.findAllByTenantId(tenantId, pageRequest);
        final  Page<UserResponse> userResponses = userPage.map(this.userMapper::toResponse);

        return PageResponse.of(userResponses);
    }

    @Override
    public void enableUser(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();

        final User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(()-> new EntityNotFoundException("User does not exist"));

        // check if user belongs to tenant
        if (!user.getTenantId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        user.setEnabled(true);
        log.info("User Enable Successfully");
    }

    @Override
    public void disableUser(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();

        final User user = this.repository.findByIdAndNotDeleted(userId)
                .orElseThrow(()-> new EntityNotFoundException("User does not exist"));

        // check if user belongs to tenant
        if (!user.getTenantId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        user.setEnabled(false);
        log.info("User Disabled Successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User with these username: " + username + "does not exist"));
    }
}
