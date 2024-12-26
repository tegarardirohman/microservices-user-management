package com.tegar.user_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class User {
    private String id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String avatarUrl;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}