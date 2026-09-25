package com.nicodev.iwanit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nicodev.iwanit.model.dto.UserResponseDto;
import com.nicodev.iwanit.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(this.userService.getUsersById(id));
  }

  @GetMapping("/all")
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    return ResponseEntity.ok(this.userService.getAllUsers());
  }

  @GetMapping
  public ResponseEntity<UserResponseDto> getUserByName(@RequestParam String username) {
    return ResponseEntity.ok(this.userService.getUserByName(username));
  }

}
