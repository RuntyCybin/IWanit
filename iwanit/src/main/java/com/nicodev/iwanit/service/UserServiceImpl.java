package com.nicodev.iwanit.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.UserAlreadyExistException;
import com.nicodev.iwanit.exception.UserInvalidCredentialsException;
import com.nicodev.iwanit.exception.UserNotFoundException;
import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.AuthRequestDto;
import com.nicodev.iwanit.model.dto.AuthResponseDto;
import com.nicodev.iwanit.model.dto.RegisterRequestDto;
import com.nicodev.iwanit.repository.UserRepository;
import com.nicodev.iwanit.security.JwtService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Override
  public AuthResponseDto signUpUser(RegisterRequestDto registerRequestDto) {
    Objects.requireNonNull(registerRequestDto, "RegisterRequestDto must not be null");
    userRepository.findByUsername(registerRequestDto.username())
        .ifPresent(user -> {
          throw new UserAlreadyExistException(registerRequestDto.username());
        });

    User user = new User(
        registerRequestDto.username(),
        passwordEncoder.encode(registerRequestDto.password()),
        registerRequestDto.role());
    userRepository.save(user);

    String token = jwtService.generateToken(user);
    return new AuthResponseDto(token);
  }

  @Override
  public AuthResponseDto signInUser(AuthRequestDto authRequestDto) {
    Objects.requireNonNull(authRequestDto, "AuthRequestDto must not be null");

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequestDto.username(), authRequestDto.password()));
    } catch (Exception e) {
      logger.error("Authentication failed for user: {}", authRequestDto.username(), e);
      throw new UserInvalidCredentialsException(authRequestDto.username());
    }

    UserDetails userDetails = userRepository
        .findByUsername(authRequestDto.username())
        .orElseThrow(() -> new UserNotFoundException(authRequestDto.username()));

    String token = jwtService.generateToken(userDetails);
    return new AuthResponseDto(token);
  }

}
