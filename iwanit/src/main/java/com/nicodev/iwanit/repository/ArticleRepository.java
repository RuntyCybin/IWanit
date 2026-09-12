package com.nicodev.iwanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Buyer;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  Optional<Article> findByName(String name);

  Optional<Article> findByBuyer(Buyer buyer);

  // JOIN FETCH avoids N+1 when loading each article's buyer
  @Query("SELECT a FROM Article a JOIN FETCH a.buyer b WHERE b.id = :buyerId")
  List<Article> findAllByBuyerId(@Param("buyerId") Long buyerId);
}
