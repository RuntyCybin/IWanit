package com.nicodev.iwanit.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nicodev.iwanit.model.PaymentMethod;
import com.nicodev.iwanit.model.StatusPayment;

public record PaymentResponseDto(
    Long id,
    Long buyerId,
    Long offerId,
    PaymentMethod paymentMethod,
    StatusPayment status,
    BigDecimal amount,
    String currency,
    LocalDateTime createdAt) {

}
