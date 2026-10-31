package com.nicodev.iwanit.service;

import java.util.List;

import com.nicodev.iwanit.model.dto.PaymentRequestDto;
import com.nicodev.iwanit.model.dto.PaymentResponseDto;

public interface PaymentService {
  PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto);

  PaymentResponseDto getPaymentById(Long paymentId);

  List<PaymentResponseDto> getAllBuyerPayments(Long buyerId);

}
