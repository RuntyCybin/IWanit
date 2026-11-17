package com.nicodev.iwanit.model.dto;

public record SellerRequestDto(String name, String email, String phoneNumber, String IBAN, String PayPal) {

  public SellerRequestDto(String name, String email, String phoneNumber, String IBAN, String PayPal) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or blank");
    }
    this.name = name;

    if (email == null || !email.contains("@") || !email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
        || email.isBlank()) {
      throw new IllegalArgumentException("Invalid or blank email address");
    }
    this.email = email;

    if (phoneNumber == null || !phoneNumber.matches("^\\+?[0-9]{10,15}$") || phoneNumber.isBlank()) {
      throw new IllegalArgumentException("Invalid or blank phone number");
    }
    this.phoneNumber = phoneNumber;

    if (IBAN != null && !IBAN.isBlank() && !IBAN.matches("^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$")) {
      throw new IllegalArgumentException("Invalid IBAN format");
    }
    this.IBAN = IBAN;

    if (PayPal != null && !PayPal.isBlank() && !PayPal.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
      throw new IllegalArgumentException("Invalid PayPal email format");
    }
    this.PayPal = PayPal;

  }

}
