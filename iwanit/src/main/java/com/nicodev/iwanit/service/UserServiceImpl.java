package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicodev.iwanit.exception.UserAlreadyExistException;
import com.nicodev.iwanit.exception.UserInvalidCredentialsException;
import com.nicodev.iwanit.exception.UserNotFoundByNameException;
import com.nicodev.iwanit.exception.UserNotFoundException;
import com.nicodev.iwanit.exception.UserNotSavedException;
import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.AuthRequestDto;
import com.nicodev.iwanit.model.dto.AuthResponseDto;
import com.nicodev.iwanit.model.dto.RegisterRequestDto;
import com.nicodev.iwanit.model.dto.UserResponseDto;
import com.nicodev.iwanit.model.mapper.UserMapper;
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
  private final UserMapper userMapper;

  /**
   * (non-Javadoc)
   * Register a new User setting up a Role
   * 
   * @see UserService#signUpUser(RegisterRequestDto)
   */
  @Override
  @Transactional
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

    try {
      userRepository.save(user);
      String token = jwtService.generateToken(user);
      return new AuthResponseDto(token, registerRequestDto.role());
    } catch (Exception e) {
      throw new UserNotSavedException(registerRequestDto.username(), e.getMessage());
    }
  }

  /**
   * (non-Javadoc)
   * Login of a User
   * 
   * @see UserService#signInUser(AuthRequestDto)
   */
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

    User user = userRepository
        .findByUsername(authRequestDto.username())
        .orElseThrow(() -> new UserNotFoundByNameException(authRequestDto.username()));

    String token = jwtService.generateToken(user);
    return new AuthResponseDto(token, user.getRole());
  }

  @Override
  public UserResponseDto getUsersById(Long id) {
    Objects.requireNonNull(id, "User id is required");

    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id.toString()));

    return this.userMapper.mapUserToUserResponseDto(user);
  }

  @Override
  public List<UserResponseDto> getAllUsers() {
    return this.userRepository.findAll().stream()
        .map(userMapper::mapUserToUserResponseDto)
        .toList();
  }

  @Override
  public UserResponseDto getUserByName(String username) {
    Objects.requireNonNull(username, "Username can not be null");

    User user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundByNameException(username));

    return this.userMapper.mapUserToUserResponseDto(user);
  }

}
