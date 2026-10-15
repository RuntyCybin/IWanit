package com.nicodev.iwanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.StatusArticle;
import com.nicodev.iwanit.model.StatusOffer;
import com.nicodev.iwanit.model.dto.ArticleWithOffersCountDto;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByName(String name);

    Optional<Article> findByIdAndStatus(Long id, StatusArticle status);

    // JOIN FETCH avoids N+1 when loading each article's buyer
    @Query("SELECT a FROM Article a JOIN FETCH a.buyer b WHERE b.id = :buyerId AND a.status = :status")
    List<Article> findAllByBuyerId(
            @Param("buyerId") Long buyerId,
            @Param("status") StatusArticle status);

    // LEFT JOIN FETCH avoids N+1 when loading an article's offers (article may have
    // none)
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.offers WHERE a.id = :id")
    Optional<Article> findByIdWithOffers(@Param("id") Long id);

    /**
     * Gets all the articles with the total number of offers received, across all
     * sellers. The result has exactly one row per article.
     * <p>
     * Articles without offers are kept (LEFT JOIN) with {@code offersCount = 0}.
     *
     * @return the articles with their offers count
     */
    @Query("""
            SELECT new com.nicodev.iwanit.model.dto.ArticleWithOffersCountDto(
                a.id, a.name, a.description, a.price, a.buyer.id, COUNT(o))
            FROM Article a
            LEFT JOIN a.offers o ON o.status = :offerStatus
            GROUP BY a.id, a.name, a.description, a.price, a.buyer.id
            """)
    List<ArticleWithOffersCountDto> findAllByBuyerIdWithOffersCount(@Param("offerStatus") StatusOffer offerStatus);

    List<Article> findAllByStatus(StatusArticle status);
}
