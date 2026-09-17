package com.nicodev.iwanit.model.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.ArticleResponseDto;
import com.nicodev.iwanit.model.dto.BuyerRequestDto;
import com.nicodev.iwanit.model.dto.BuyerResponseDto;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BuyerMapper {

  private final ArticleMapper articleMapper;

  public Buyer mapBuyerRequestDtoToBuyer(BuyerRequestDto buyerRequestDto, User user) {
    return new Buyer(
        buyerRequestDto.name(),
        buyerRequestDto.email(),
        buyerRequestDto.phoneNumber(),
        user);
  }

  public BuyerResponseDto mapBuyerToResponseDto(Buyer buyer) {
    List<ArticleResponseDto> articles = buyer.getArticles() == null
        ? List.of()
        : buyer.getArticles().stream().map(articleMapper::toArticleResponseDto).toList();

    return new BuyerResponseDto(
        buyer.getId(),
        buyer.getName(),
        buyer.getEmail(),
        buyer.getPhoneNumber(),
        buyer.getUser().getId(),
        articles);
  }

}
