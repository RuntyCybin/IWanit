package com.nicodev.iwanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nicodev.iwanit.model.AcceptedOffer;

public interface AcceptedOfferRepository extends JpaRepository<AcceptedOffer, Long> {

  Optional<AcceptedOffer> findByArticleId(Long articleId);

  boolean existsByArticleId(Long articleId);

  // Fetches what the mapper reads in the same query to avoid one select per row
  @EntityGraph(attributePaths = { "offer", "offer.seller", "article" })
  List<AcceptedOffer> findAllByArticleBuyerIdOrderByAcceptedAtDesc(Long buyerId);

}
