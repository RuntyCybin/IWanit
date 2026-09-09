package com.nicodev.iwanit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.AuthRequestDto;
import com.nicodev.iwanit.model.dto.AuthResponseDto;
import com.nicodev.iwanit.model.dto.RegisterRequestDto;
import com.nicodev.iwanit.repository.UserRepository;
import com.nicodev.iwanit.security.JwtService;
import com.nicodev.iwanit.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
    return ResponseEntity.ok(userService.signUpUser(registerRequestDto));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
    return ResponseEntity.ok(userService.signInUser(authRequestDto));
  }
}
