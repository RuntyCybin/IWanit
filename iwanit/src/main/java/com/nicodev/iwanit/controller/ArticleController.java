package com.nicodev.iwanit.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicodev.iwanit.model.dto.ArticleRequestDto;
import com.nicodev.iwanit.model.dto.ArticleResponseDto;
import com.nicodev.iwanit.service.ArticleService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/articles")
@AllArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  @PostMapping
  public ResponseEntity<ArticleResponseDto> create(@RequestBody ArticleRequestDto articleRequestDto) {
    var cfreatedArticle = this.articleService.createArticle(articleRequestDto);
    return ResponseEntity.created(URI.create("/v1/articles/" + cfreatedArticle.id())).body(cfreatedArticle);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ArticleResponseDto> getArticle(@PathVariable Long id) {
    return ResponseEntity.ok(this.articleService.getArticleById(id));
  }

  @GetMapping("/buyer/{buyerId}")
  public ResponseEntity<List<ArticleResponseDto>> getAllBuyerArticles(@PathVariable Long buyerId) {
    return ResponseEntity.ok(this.articleService.getAllBuyerArticles(buyerId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ArticleResponseDto> updateArticle(@PathVariable Long id,
      @RequestBody ArticleRequestDto articleRequestDto) {
    return ResponseEntity.ok(this.articleService.updateArticle(articleRequestDto, id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
    this.articleService.deleteArticle(id);
    return ResponseEntity.noContent().build();
  }

}
