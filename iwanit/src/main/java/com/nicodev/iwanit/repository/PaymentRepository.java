package com.nicodev.iwanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicodev.iwanit.model.Payment;
import com.nicodev.iwanit.model.StatusPayment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findAllByBuyerIdOrderByCreatedAtDesc(Long buyerId);

  boolean existsByOfferIdAndStatus(Long offerId, StatusPayment status);

}
