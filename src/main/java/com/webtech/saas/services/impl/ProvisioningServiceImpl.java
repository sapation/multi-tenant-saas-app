package com.webtech.saas.services.impl;

import com.webtech.saas.entities.Tenant;
import com.webtech.saas.exceptions.TenantProvisionException;
import com.webtech.saas.services.ProvisioningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProvisioningServiceImpl implements ProvisioningService {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public void provisionTenant(Tenant tenant) {
        final String schemaName = "tenant_" + tenant.getCompanyCode().toLowerCase();

        try {
            log.info("Provisioning tenant: {} (schema: {})", tenant.getCompanyName(), schemaName);

            // 1. Create the postgres
            createSchema(schemaName);
            log.info("Schema {} Created successfully ", schemaName);
            // 2. Run the flyway migrations for the schema
            runTenantMigration(schemaName);

            // 3. Initialised the default data (Optional)
            initialisedDefaultData(schemaName, tenant);
        } catch (Exception e) {
            log.error("Error Provisioning tenant: {}", tenant.getCompanyName(), e);

            // rollback
            try {
                dropSchema(schemaName);
            } catch (Exception ex) {
                log.error("Error droping schema: {}", schemaName, ex);
            }

            throw new TenantProvisionException("Fail to provision tenant");
        }
    }

    private void initialisedDefaultData(String schemaName, Tenant tenant) {
        // do what you need to initialised first
    }

    private void runTenantMigration(String schemaName) {
        log.info("Running tenant migration for schema: {}", schemaName);
        final Flyway tenantFlyway = Flyway.configure()
                .dataSource(this.dataSource)
                .schemas(schemaName)
                .locations("classpath:db/migration/tenant")
                .baselineOnMigrate(true)
                .table("tenant_flyway_schema_history")
                .validateOnMigrate(true)
                .cleanDisabled(true)
                .load();

        log.info("Tenant Flyway migration started");
        tenantFlyway.migrate();
        log.info("Tenant Flyway migration completed");
    }

    private void createSchema(String schemaName) {
        final String sql = String.format("CREATE SCHEMA IF NOT EXIST %s", schemaName);
        this.jdbcTemplate.execute(sql);
    }

    private void dropSchema(String schemaName) {
        final String sql = String.format("DROP SCHEMA IF EXIST %s", schemaName);
        this.jdbcTemplate.execute(sql);
    }
}
