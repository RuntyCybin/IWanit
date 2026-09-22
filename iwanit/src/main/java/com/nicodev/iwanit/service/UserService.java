package com.nicodev.iwanit.service;

import java.util.List;

import com.nicodev.iwanit.model.dto.AuthRequestDto;
import com.nicodev.iwanit.model.dto.AuthResponseDto;
import com.nicodev.iwanit.model.dto.RegisterRequestDto;
import com.nicodev.iwanit.model.dto.UserResponseDto;

public interface UserService {
  AuthResponseDto signUpUser(RegisterRequestDto registerRequestDto);

  AuthResponseDto signInUser(AuthRequestDto authRequestDto);

  UserResponseDto getUsersById(Long id);

  List<UserResponseDto> getAllUsers();

  UserResponseDto getUserByName(String username);
}
