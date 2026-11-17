package com.nicodev.iwanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nicodev.iwanit.model.Offer;
import com.nicodev.iwanit.model.StatusOffer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

  Optional<Offer> findByArticleIdAndSellerId(Long articleId, Long sellerId);

  @Query("SELECT o FROM Offer o LEFT JOIN FETCH o.article WHERE o.article.id = :articleId AND o.status = :status")
  List<Offer> findAllByArticleIdAndStatus(Long articleId, StatusOffer status);

  @Query("SELECT o FROM Offer o LEFT JOIN FETCH o.seller WHERE o.seller.id = :sellerId AND o.status = :status")
  List<Offer> findAllBySellerIdAndStatus(Long sellerId, StatusOffer status);

}
