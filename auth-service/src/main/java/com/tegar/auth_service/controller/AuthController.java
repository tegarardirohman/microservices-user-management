package com.tegar.auth_service.controller;

import com.tegar.auth_service.model.dto.request.AuthRequest;
import com.tegar.auth_service.model.dto.response.LoginResponse;
import com.tegar.auth_service.model.dto.response.SignUpResponse;
import com.tegar.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Validated @RequestBody AuthRequest request) {
        SignUpResponse response = authService.signup(request);
        return ResponseEntity.status(201).body(response); // Status 201 Created
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody AuthRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response); // Status 200 OK
    }
}
