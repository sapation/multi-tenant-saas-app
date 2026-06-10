package com.webtech.saas.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TenantSchemaResolver {
    private final JdbcTemplate jdbcTemplate;

    private final static String PUBLIC_SCHEMA = "public";

    @Cacheable(value = "tenantSchemas", key = "#tenantId")
    public String resolveTenantSchema(String tenantId) {
        if (tenantId == null) {
            return PUBLIC_SCHEMA;
        }

        try {
            final String company_code = this.jdbcTemplate.queryForObject(
                    "SELECT company_code FROM public.tenants WHERE id = ? and deleted = false", String.class, tenantId);

            if (company_code != null) {
                final String schemaName = "tenant_" + company_code;
                log.info("Tenant Schema: resolved: {} for tenant: {}", schemaName, tenantId);
                return  schemaName;
            }

            log.warn("Tenant schema not found for tenant: {} using public schema", tenantId);
        } catch (Exception e) {
            log.error("Error in resolving tenant schema for tenant: {}", tenantId);
        }

        return PUBLIC_SCHEMA;
    }
}
