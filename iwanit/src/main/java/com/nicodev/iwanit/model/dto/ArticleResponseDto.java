package com.nicodev.iwanit.model.dto;

import java.util.List;

public record ArticleResponseDto(
    Long id,
    String title,
    String content,
    Double price,
    Long buyer,
    List<OfferResponseDto> offers) {

}
