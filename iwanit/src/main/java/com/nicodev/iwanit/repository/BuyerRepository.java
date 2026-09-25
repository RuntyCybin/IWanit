package com.nicodev.iwanit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicodev.iwanit.model.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {

  Optional<Buyer> findByEmail(String email);

}
