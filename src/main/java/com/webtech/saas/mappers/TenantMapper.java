package com.webtech.saas.mappers;

import com.webtech.saas.entities.Tenant;
import com.webtech.saas.requests.TenantRequest;
import com.webtech.saas.responses.TenantResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TenantMapper {
    public Tenant toEntity(TenantRequest request) {
        return Tenant.builder()
                .companyCode(request.getCompanyName())
                .companyCode(request.getCompanyCode())
                .createdAt(LocalDateTime.now())
                .email(request.getEmail())
                .adminFullName(request.getAdminFullName())
                .adminEmail(request.getAdminEmail())
                .adminUsername(request.getAdminUsername())
                .build();
    }

    public TenantResponse toResponse(Tenant tenant) {
        return TenantResponse.builder()
                .tenantId(tenant.getId())
                .companyName(tenant.getCompanyName())
                .companyCode(tenant.getCompanyCode())
                .email(tenant.getEmail())
                .adminFullName(tenant.getAdminFullName())
                .adminUsername(tenant.getAdminUsername())
                .adminEmail(tenant.getAdminEmail())
                .createdAt(tenant.getCreatedAt())
                .status(tenant.getStatus())
                .build();
    }
}
