package com.nicodev.iwanit.model.mapper;

import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.Payment;
import com.nicodev.iwanit.model.dto.PaymentResponseDto;

@Component
public class PaymentMapper {

  public PaymentResponseDto mapPaymentToResponseDto(Payment payment) {
    return new PaymentResponseDto(payment.getId(), payment.getBuyer().getId(), payment.getOffer().getId(),
        payment.getPaymentMethod(), payment.getStatus(), payment.getAmount(), payment.getCurrency(),
        payment.getCreatedAt());
  }
}
