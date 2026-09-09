package com.nicodev.iwanit.service;

import com.nicodev.iwanit.model.dto.AuthRequestDto;
import com.nicodev.iwanit.model.dto.AuthResponseDto;
import com.nicodev.iwanit.model.dto.RegisterRequestDto;

public interface UserService {
  AuthResponseDto signUpUser(RegisterRequestDto registerRequestDto);

  AuthResponseDto signInUser(AuthRequestDto authRequestDto);
}
