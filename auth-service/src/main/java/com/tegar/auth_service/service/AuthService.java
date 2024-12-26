package com.tegar.auth_service.service;

import com.tegar.auth_service.model.dto.request.AuthRequest;
import com.tegar.auth_service.model.dto.response.LoginResponse;
import com.tegar.auth_service.model.dto.response.SignUpResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    SignUpResponse signup(AuthRequest request);

    LoginResponse login(AuthRequest request);
}
