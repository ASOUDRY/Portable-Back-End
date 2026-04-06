package com.soudry.portable_back_end.user.admin;

public record UserResponse(
        String id,
        String username,
        String email,
        String role
) {}