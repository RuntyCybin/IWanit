package com.nicodev.iwanit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicodev.iwanit.model.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {

  Optional<Seller> findByEmail(String email);

  Optional<Seller> findByUserId(Long userId);

}
