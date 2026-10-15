package com.nicodev.iwanit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicodev.iwanit.model.AcceptedOffer;

public interface AcceptedOfferRepository extends JpaRepository<AcceptedOffer, Long> {

  Optional<AcceptedOffer> findByArticleId(Long articleId);

  boolean existsByArticleId(Long articleId);

}
