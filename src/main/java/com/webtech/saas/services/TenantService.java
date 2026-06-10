package com.webtech.saas.services;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.entities.Tenant;
import com.webtech.saas.requests.TenantRequest;
import com.webtech.saas.responses.TenantResponse;

public interface TenantService {
    void registerTenant(final TenantRequest request);

    void approveTenant(final String tenantId);

    void activateTenant(final String tenantId);

    void deactivateTenant(final String tenantId);

    void suspendTenant(final String tenantId);

    PageResponse<TenantResponse> findAll(final int page, int size);
}
