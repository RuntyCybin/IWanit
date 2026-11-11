package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.ArticleNotAvailableException;
import com.nicodev.iwanit.exception.BuyerNotFoundException;
import com.nicodev.iwanit.exception.OfferNotFoundException;
import com.nicodev.iwanit.exception.PaymentAlreadyCompletedException;
import com.nicodev.iwanit.exception.PaymentNotFoundException;
import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.Offer;
import com.nicodev.iwanit.model.Payment;
import com.nicodev.iwanit.model.Role;
import com.nicodev.iwanit.model.StatusArticle;
import com.nicodev.iwanit.model.StatusOffer;
import com.nicodev.iwanit.model.StatusPayment;
import com.nicodev.iwanit.model.dto.PaymentRequestDto;
import com.nicodev.iwanit.model.dto.PaymentResponseDto;
import com.nicodev.iwanit.model.mapper.PaymentMapper;
import com.nicodev.iwanit.repository.ArticleRepository;
import com.nicodev.iwanit.repository.BuyerRepository;
import com.nicodev.iwanit.repository.OfferRepository;
import com.nicodev.iwanit.repository.PaymentRepository;
import com.nicodev.iwanit.security.AuthenticatedUser;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final BuyerRepository buyerRepository;
  private final OfferRepository offerRepository;
  private final PaymentMapper paymentMapper;
  private final AuthenticatedUser authenticatedUser;
  private final ArticleRepository articleRepository;

  /**
   * The buyer of an article pays the offer they accepted for it.
   * A new payment is created for every attempt, so a buyer can retry after a
   * failed one, but an offer can only have one completed payment.
   */
  @Override
  @Transactional
  public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
    Objects.requireNonNull(paymentRequestDto, "PaymentRequestDto must not be null");

    Buyer buyer = buyerRepository.findById(paymentRequestDto.buyerId())
        .orElseThrow(() -> new BuyerNotFoundException(paymentRequestDto.buyerId()));
    Offer offer = offerRepository.findById(paymentRequestDto.offerId())
        .orElseThrow(() -> new OfferNotFoundException(paymentRequestDto.offerId()));

    if (!authenticatedUser.isOwner(Role.BUYER, buyer.getUser())) {
      throw new AccessDeniedException("Buyers can only create their own payments");
    }
    Article article = offer.getArticle();
    if (!article.getBuyer().getId().equals(buyer.getId())) {
      throw new AccessDeniedException("Only the article's buyer can pay its offers");
    }
    if (paymentRepository.existsByOfferIdAndStatus(offer.getId(), StatusPayment.COMPLETED)) {
      throw new PaymentAlreadyCompletedException(offer.getId());
    }
    // A bought article can only receive retries for the offer that was paid
    if (article.getStatus() == StatusArticle.BOUGHT && offer.getStatus() != StatusOffer.ACCEPTED) {
      throw new ArticleNotAvailableException(article.getId());
    }

    // The article is kept as BOUGHT instead of deleted, payments reference its offers
    if (article.getStatus() != StatusArticle.BOUGHT) {
      article.getOffers().stream()
          .filter(other -> !other.getId().equals(offer.getId()))
          .forEach(other -> other.setStatus(StatusOffer.REJECTED));
      offer.setStatus(StatusOffer.ACCEPTED);
      article.setStatus(StatusArticle.BOUGHT);
      offerRepository.saveAll(article.getOffers());
      articleRepository.save(article);
    }

    Payment payment = paymentRepository.save(new Payment(paymentRequestDto.paymentMethod(), buyer, offer));
    return paymentMapper.mapPaymentToResponseDto(payment);
  }

  @Override
  public PaymentResponseDto getPaymentById(Long paymentId) {
    Objects.requireNonNull(paymentId, "Payment ID must not be null");
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new PaymentNotFoundException(paymentId));

    // The buyer who paid or the seller who receives the payment
    if (!authenticatedUser.isOwner(Role.BUYER, payment.getBuyer().getUser())
        && !authenticatedUser.isOwner(Role.SELLER, payment.getOffer().getSeller().getUser())) {
      throw new AccessDeniedException("Not allowed to view this payment");
    }

    return paymentMapper.mapPaymentToResponseDto(payment);
  }

  @Override
  public List<PaymentResponseDto> getAllBuyerPayments(Long buyerId) {
    Objects.requireNonNull(buyerId, "Buyer ID must not be null");

    Buyer buyer = buyerRepository.findById(buyerId)
        .orElseThrow(() -> new BuyerNotFoundException(buyerId));

    if (!authenticatedUser.isOwner(Role.BUYER, buyer.getUser())) {
      throw new AccessDeniedException("Buyers can only list their own payments");
    }

    return paymentRepository.findAllByBuyerIdOrderByCreatedAtDesc(buyerId).stream()
        .map(paymentMapper::mapPaymentToResponseDto)
        .toList();
  }

}
