package com.webtech.saas.services;

import com.webtech.saas.entities.Tenant;

public interface ProvisioningService {
    void provisionTenant(final Tenant tenant);
}
