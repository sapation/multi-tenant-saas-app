package com.webtech.saas.responses;

import com.webtech.saas.entities.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private UserRole role;
}
