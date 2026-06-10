package com.webtech.saas.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantRequest {
    @NotBlank(message = "Company name should not be empty")
    private String companyName;

    @NotBlank(message = "Company code should not be empty")
    private String companyCode;

    @NotBlank(message = "Email should not be empty")
    private String email;

    @NotBlank(message = "Admin fullname should not be empty")
    private String adminFullName;

    @NotBlank(message = "Admin email should not be empty")
    private String adminEmail;

    @NotBlank(message = "Admin username should not be empty")
    private String adminUsername;

    @NotBlank(message = "Admin Password should not be empty")
    private String adminPassword;
}
