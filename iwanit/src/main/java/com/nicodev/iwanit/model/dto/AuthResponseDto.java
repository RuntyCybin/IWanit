package com.nicodev.iwanit.model.dto;

import com.nicodev.iwanit.model.Role;

public record AuthResponseDto(String token, Role role) {

}
