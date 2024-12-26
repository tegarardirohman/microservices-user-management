package com.tegar.auth_service.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
    private String email;
    private String role;
}
