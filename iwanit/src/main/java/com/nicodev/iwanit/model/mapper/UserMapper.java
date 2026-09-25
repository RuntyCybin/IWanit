package com.nicodev.iwanit.model.mapper;

import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.UserResponseDto;

@Component
public class UserMapper {

  public UserResponseDto mapUserToUserResponseDto(User user) {
    return new UserResponseDto(user.getId(), user.getUsername(), user.getRole());
  }
}
