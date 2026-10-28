package com.nicodev.iwanit.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
public class PaymentsController {

  @PostMapping
  public String createPayment() {

    return "Payment created successfully";
  }

}
