package com.nicodev.iwanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.dto.ArticleWithOffersCountDto;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByName(String name);

    Optional<Article> findByBuyer(Buyer buyer);

    // JOIN FETCH avoids N+1 when loading each article's buyer
    @Query("SELECT a FROM Article a JOIN FETCH a.buyer b WHERE b.id = :buyerId")
    List<Article> findAllByBuyerId(@Param("buyerId") Long buyerId);

    // LEFT JOIN FETCH avoids N+1 when loading an article's offers (article may have
    // none)
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.offers WHERE a.id = :id")
    Optional<Article> findByIdWithOffers(@Param("id") Long id);

    /**
     * Gets the articles of a buyer with the number of offers received.
     * <p>
     * The result has one row per article and seller, where {@code offersCount} is
     * the number of offers that seller made on that article. An article with
     * offers from several sellers therefore appears several times.
     * <p>
     * Articles without offers are kept (LEFT JOIN) and appear once with
     * {@code sellerId = null} and {@code offersCount = 0}.
     *
     * @param buyerId id of the buyer who owns the articles
     * @return the articles with their offers count, per seller
     */
    @Query("""
            SELECT new com.nicodev.iwanit.model.dto.ArticleWithOffersCountDto(
                a.id, a.name, a.description, a.price, a.buyer.id, o.seller.id, COUNT(o))
            FROM Article a
            LEFT JOIN a.offers o
            GROUP BY a.id, a.name, a.description, a.price, a.buyer.id, o.seller.id
            """)
    List<ArticleWithOffersCountDto> findAllByBuyerIdWithOffersCount();
}
