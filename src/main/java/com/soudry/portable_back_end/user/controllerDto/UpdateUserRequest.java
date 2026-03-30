package com.soudry.portable_back_end.user.controllerDto;

public record UpdateUserRequest(String username, String password, String email) {}