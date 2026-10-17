package com.nicodev.iwanit.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "buyers")
public class Buyer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name_buyer", nullable = false)
  private String name;
  @Column(name = "email_buyer", nullable = false)
  private String email;
  @Column(name = "phone_number_buyer", nullable = false)
  private String phoneNumber;
  @Column(name = "address", nullable = false, unique = true)
  private String address;
  @Column(name = "cp", nullable = false)
  private String cp;
  @Column(name = "country", nullable = false)
  private String country;

  @OneToOne
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  @OneToMany(mappedBy = "buyer")
  private List<Article> articles;

  public Buyer() {

  }

  public Buyer(String name, String email, String phoneNumber, String address, String cp, String country) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.cp = cp;
    this.country = country;
  }

  public Buyer(String name, String email, String phoneNumber, String address, String cp, String country, User user) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.cp = cp;
    this.country = country;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Article> getArticles() {
    return articles;
  }

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCp() {
    return cp;
  }

  public void setCp(String cp) {
    this.cp = cp;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
