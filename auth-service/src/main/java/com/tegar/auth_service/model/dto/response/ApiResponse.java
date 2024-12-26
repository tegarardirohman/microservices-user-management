package com.tegar.auth_service.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String status;
    private T body;
}
