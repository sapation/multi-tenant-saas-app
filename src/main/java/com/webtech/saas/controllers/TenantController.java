package com.webtech.saas.controllers;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.entities.Tenant;
import com.webtech.saas.responses.TenantResponse;
import com.webtech.saas.services.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/producties")
@RequiredArgsConstructor
@Slf4j
public class TenantController {
    private final TenantService tenantService;

    @PatchMapping("/approve/{tenantId}")
    public ResponseEntity<Void> approveTenant(@PathVariable("tenantId") String tenantId) {
        this.tenantService.approveTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate/{tenantId}")
    public ResponseEntity<Void> activateTenant(@PathVariable("tenantId") String tenantId) {
        this.tenantService.suspendTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate/{tenantId}")
    public ResponseEntity<Void> deactivateTenant(@PathVariable("tenantId") String tenantId) {
        this.tenantService.deactivateTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/suspend/{tenantId}")
    public ResponseEntity<Void> suspendTenant(@PathVariable("tenantId") String tenantId) {
        this.tenantService.suspendTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<TenantResponse>> fetchAllTenants(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(this.tenantService.findAll(page, size));
    }
}
