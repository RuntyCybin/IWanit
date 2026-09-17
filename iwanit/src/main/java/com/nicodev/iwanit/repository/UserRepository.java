package com.nicodev.iwanit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicodev.iwanit.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
