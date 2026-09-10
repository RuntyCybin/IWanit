package com.nicodev.iwanit.model.dto;

public record ArticleResponseDto(Long id, String title, String content, Long buyerId, Long sellerId) {

}
