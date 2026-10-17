package com.nicodev.iwanit.model.dto;

public record BuyerRequestDto(String name, String email, String phoneNumber, String address, String cp,
    String country) {

  public BuyerRequestDto(String name, String email, String phoneNumber, String address, String cp, String country) {

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

    if (address == null || address.isBlank()) {
      throw new IllegalArgumentException("Address cannot be null or blank");
    }
    this.address = address;

    if (cp == null || !cp.matches("^[0-9]{5}$") || cp.isBlank()) {
      throw new IllegalArgumentException("Invalid or blank postal code (cp)");
    }
    this.cp = cp;

    if (country == null || country.isBlank()) {
      throw new IllegalArgumentException("Country cannot be null or blank");
    }
    this.country = country;
  }

}
