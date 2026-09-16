package com.nicodev.iwanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nicodev.iwanit.model.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

  Optional<Offer> findByArticleIdAndSellerId(Long articleId, Long sellerId);

  @Query("SELECT o FROM Offer o LEFT JOIN FETCH o.article WHERE o.article.id = :articleId")
  List<Offer> findAllByArticleId(Long articleId);

  @Query("SELECT o FROM Offer o LEFT JOIN FETCH o.seller WHERE o.seller.id = :sellerId")
  List<Offer> findAllBySellerId(Long sellerId);

}
