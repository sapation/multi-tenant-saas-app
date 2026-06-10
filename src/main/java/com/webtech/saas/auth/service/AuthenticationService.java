package com.webtech.saas.auth.service;

import com.webtech.saas.auth.request.LoginRequest;
import com.webtech.saas.auth.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(final LoginRequest request);
}
