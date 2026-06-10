package com.webtech.saas.services.impl;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.entities.Tenant;
import com.webtech.saas.entities.TenantStatus;
import com.webtech.saas.entities.User;
import com.webtech.saas.entities.UserRole;
import com.webtech.saas.exceptions.DuplicateResourceException;
import com.webtech.saas.exceptions.InvalidRequestException;
import com.webtech.saas.mappers.TenantMapper;
import com.webtech.saas.repositories.TenantRepository;
import com.webtech.saas.repositories.UserRepository;
import com.webtech.saas.requests.TenantRequest;
import com.webtech.saas.responses.TenantResponse;
import com.webtech.saas.services.ProvisioningService;
import com.webtech.saas.services.TenantService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantServiceImpl implements TenantService {
    private final TenantRepository repository;
    private final TenantMapper tenantMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProvisioningService provisioningService;

    @Override
    public void registerTenant(TenantRequest request) {
        // check if the tenant already exist
        if (this.repository.existByCompanyCode(request.getCompanyCode())) {
            throw new DuplicateResourceException("Tenant Already Exist");
        }

        //check if the email already exist
        if (this.repository.existByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exist");
        }

        //create tenant entity
        final Tenant tenant = this.tenantMapper.toEntity(request);
        tenant.setAdminPassword(this.passwordEncoder.encode(request.getAdminPassword()));
        this.repository.save(tenant);
        log.info("Tenant created successfully");
    }

    @Override
    public void approveTenant(String tenantId) {
        final Tenant tenant = this.repository.findById(tenantId)
                .orElseThrow(()-> new EntityNotFoundException("Tenant does not exist"));
        tenant.setStatus(TenantStatus.ACTIVE);
        this.repository.save(tenant);

        try {
            // provision tenant
            this.provisioningService.provisionTenant(tenant);

            //create admin user
            createAdminUser(tenant);
        } catch (final Exception e) {
            log.error("An Error occur during provisioning of the tenant");
            rollbackTenantStatus(tenant);
        }
    }

    @Override
    public void activateTenant(String tenantId) {
        final Tenant tenant = this.repository.findById(tenantId)
                .orElseThrow(()-> new EntityNotFoundException("Tenant does not exist"));

        if (tenant.getStatus() != TenantStatus.PENDING) {
            throw new InvalidRequestException("Tenant is not pending");
        }

        tenant.setStatus(TenantStatus.ACTIVE);
        this.repository.save(tenant);
    }

    @Override
    public void deactivateTenant(String tenantId) {
        final Tenant tenant = this.repository.findById(tenantId)
                .orElseThrow(()-> new EntityNotFoundException("Tenant does not exist"));

        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new InvalidRequestException("Tenant is not pending");
        }

        tenant.setStatus(TenantStatus.INACTIVE);
        this.repository.save(tenant);
    }

    @Override
    public void suspendTenant(String tenantId) {
        final Tenant tenant = this.repository.findById(tenantId)
                .orElseThrow(()-> new EntityNotFoundException("Tenant does not exist"));

        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new InvalidRequestException("Tenant is not pending");
        }

        tenant.setStatus(TenantStatus.SUSPENDED);
        this.repository.save(tenant);
    }

    @Override
    public PageResponse<TenantResponse> findAll(int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<Tenant> tenants = this.repository.findAll(pageRequest);
        final Page<TenantResponse> pageResponse = tenants.map(this.tenantMapper::toResponse);

        return PageResponse.of(pageResponse);
    }

    private void rollbackTenantStatus(Tenant tenant) {
        tenant.setStatus(TenantStatus.PENDING);
        this.repository.save(tenant);
    }

    private void createAdminUser(Tenant tenant) {
        if (this.userRepository.existsByUsername(tenant.getAdminUsername())) {
            throw  new DuplicateResourceException("User already exist");
        }

        final User adminUser = User.builder()
                .username(tenant.getAdminUsername())
                .email(tenant.getEmail())
                .firstName(extractFirstName(tenant.getAdminFullName()))
                .lastName(extractLastName(tenant.getAdminFullName()))
                .password(tenant.getAdminPassword())
                .role(UserRole.ROLE_COMPANY_ADMIN)
                .tenant(tenant)
                .deleted(false)
                .build();

        this.userRepository.save(adminUser);
        log.info("Admin user created successfully");
    }

    private String extractLastName(String adminFullName) {
        return adminFullName.split(" ").length > 1 ? adminFullName.split(" ")[1] : adminFullName;
    }

    private String extractFirstName(String adminFullName) {
        return adminFullName.split(" ")[0];
    }
}
