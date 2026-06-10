package com.webtech.saas.auth;

import com.webtech.saas.auth.request.LoginRequest;
import com.webtech.saas.auth.response.LoginResponse;
import com.webtech.saas.auth.service.AuthenticationService;
import com.webtech.saas.requests.TenantRequest;
import com.webtech.saas.services.TenantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication Api")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TenantService tenantService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        final LoginResponse loginResponse = this.authenticationService.login(request);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid @RequestBody TenantRequest request) {
        this.tenantService.registerTenant(request);

        return ResponseEntity.ok().build();
    }
}
