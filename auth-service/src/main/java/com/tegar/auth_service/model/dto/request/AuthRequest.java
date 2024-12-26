package com.tegar.auth_service.model.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
