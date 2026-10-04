package com.nicodev.iwanit.model.dto;

public record BuyerRequestDto(String name, String email, String phoneNumber) {

  public BuyerRequestDto(String name, String email, String phoneNumber) {

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

  }

}
