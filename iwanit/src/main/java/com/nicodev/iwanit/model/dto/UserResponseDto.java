package com.nicodev.iwanit.model.dto;

import java.util.Objects;

import com.nicodev.iwanit.model.Role;

public record UserResponseDto(Long id, String username, Role role) {

  public UserResponseDto {
    Objects.requireNonNull(username, "Username must not be null");
    Objects.requireNonNull(role, "Role must not be null");
  }

}
