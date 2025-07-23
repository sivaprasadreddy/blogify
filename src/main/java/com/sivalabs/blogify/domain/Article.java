package com.sivalabs.blogify.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_id_generator")
    @SequenceGenerator(name = "article_id_generator", sequenceName = "article_id_seq")
    private Long id;

    @Column(nullable = false)
    private String prompt;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(name = "enh_content")
    private String enhancedContent;
    @Column(name = "slug")
    private String slug;
    @Column(name = "images", columnDefinition = "text[]")
    private List<String> images;
    @Column(name = "categories", columnDefinition = "text[]")
    private Set<String> categories;
    @Column(name = "tags", columnDefinition = "text[]")
    private Set<String> tags;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public Article() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEnhancedContent() {
        return enhancedContent;
    }

    public void setEnhancedContent(String enhancedContent) {
        this.enhancedContent = enhancedContent;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
