package com.webtech.saas.repositories;

import com.webtech.saas.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {
    boolean existByEmail(String email);

    boolean existByCompanyCode(String companyCode);
}
