package com.nicodev.iwanit.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "accepted_offers")
public class AcceptedOffer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "offer_id", nullable = false, unique = true)
  private Offer offer;

  @OneToOne
  @JoinColumn(name = "article_id", nullable = false, unique = true)
  private Article article;

  @Column(name = "accepted_at", nullable = false)
  private LocalDateTime acceptedAt;

  public AcceptedOffer() {
  }

  public AcceptedOffer(Offer offer) {
    this.offer = offer;
    this.article = offer.getArticle();
    this.acceptedAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Offer getOffer() {
    return offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public LocalDateTime getAcceptedAt() {
    return acceptedAt;
  }

  public void setAcceptedAt(LocalDateTime acceptedAt) {
    this.acceptedAt = acceptedAt;
  }
}
