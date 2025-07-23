package com.sivalabs.blogify.web;

import com.sivalabs.blogify.domain.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ArticleForm(
            @NotNull(message = "Article id is required")
            Long id,
            @NotBlank(message = "Title is required")
            String title,
            @NotBlank(message = "Slug is required")
            String slug,
            Set<String> categories,
            Set<String> tags,
            @NotBlank(message = "Content is required")
            String content,
            String enhancedContent) {
        public static ArticleForm from(Article article) {
            return new ArticleForm(
                    article.getId(),
                    article.getTitle(),
                    article.getSlug(),
                    article.getCategories(),
                    article.getTags(),
                    article.getContent(),
                    article.getEnhancedContent()
            );
        }
    }