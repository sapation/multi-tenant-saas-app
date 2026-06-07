package com.webtech.saas.requests;

import com.webtech.saas.entities.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private UserRole role;
}
