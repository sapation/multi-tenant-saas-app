package com.webtech.saas.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        final String tenantId  = resolveHeader(httpRequest);

        if (tenantId == null || tenantId.isBlank()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("application./json");
            httpResponse.getWriter().write("{\"error\":\"Tenant ID is missing in the request header. Please add the header X-Tenant-ID.\"}");
            return;
        }

        try {
            TenantContext.setCurrentTenant(tenantId);
            chain.doFilter(request,response);
        } finally {
            TenantContext.clear();
        }
    }

    private String resolveHeader(final HttpServletRequest httpRequest) {
        final String tenantId = httpRequest.getHeader(TENANT_HEADER);
        if (tenantId != null && !tenantId.isBlank()) {
            return tenantId.toLowerCase();
        }

        return null;
    }
}
