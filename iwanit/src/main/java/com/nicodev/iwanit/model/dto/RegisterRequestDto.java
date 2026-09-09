package com.nicodev.iwanit.model.dto;

import com.nicodev.iwanit.model.Role;

public record RegisterRequestDto(String username, String password, Role role) {

}
