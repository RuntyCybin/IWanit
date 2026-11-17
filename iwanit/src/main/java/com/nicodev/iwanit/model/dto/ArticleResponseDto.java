package com.nicodev.iwanit.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record ArticleResponseDto(
    Long id,
    String title,
    String content,
    BigDecimal price,
    Long buyer,
    List<OfferResponseDto> offers) {

}
