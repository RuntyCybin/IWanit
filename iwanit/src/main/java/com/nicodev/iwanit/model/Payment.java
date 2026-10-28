package com.nicodev.iwanit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

  private String id;
  private Buyer buyer;
  private Offer offer;

  public Payment(String id, Buyer buyer, Offer offer) {
    this.id = id;
    this.buyer = buyer;
    this.offer = offer;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Buyer getBuyer() {
    return buyer;
  }

  public void setBuyer(Buyer buyer) {
    this.buyer = buyer;
  }

  public Offer getOffer() {
    return offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
  }
}
