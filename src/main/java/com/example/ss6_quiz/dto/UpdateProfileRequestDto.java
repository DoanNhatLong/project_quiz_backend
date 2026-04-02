package com.example.ss6_quiz.dto;

public record UpdateProfileRequestDto(
        Long id,
        String username,
        String oldPassword,
        String newPassword
) {}