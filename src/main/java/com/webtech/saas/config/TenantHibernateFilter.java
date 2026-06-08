package com.webtech.saas.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
public class TenantHibernateFilter {

    @PersistenceContext
    private EntityManager entityManager;

    @Before("execution(* com.webtech.saas.services.impl.*.*(..))")
    public void activateFilter() {
        final String tenantId = TenantContext.getCurrentTenant();
        if(tenantId != null) {
            final Session session = entityManager.unwrap(Session.class);

            // activate the filter to inject the tenant id
            session.enableFilter("tenantFilter")
                    .setParameter("tenantId", tenantId);
        }
    }
}
