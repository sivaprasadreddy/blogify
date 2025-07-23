package com.sivalabs.blogify.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ArticleUtils {

    private static final Logger log = LoggerFactory.getLogger(ArticleUtils.class);

    public static String generateHugoArticle(Article article, boolean enhanced) {
        String content = enhanced ? article.getEnhancedContent() : article.getContent();
        if (content == null) {
            throw new IllegalArgumentException("Content is required");
        }
        String publishedDate = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx"));
        String categories = String.join(", ", article.getCategories());
        String tags = String.join(", ", article.getTags());
        String image = article.getImages() == null || article.getImages().isEmpty() ? "" : article.getImages().getFirst();
        String slug = article.getSlug();
        if (slug.startsWith("/")) {
            slug = slug.substring(1);
        }
        return """
                ---
                title: "%s"
                author: Siva
                images: ["%s"]
                type: post
                draft: false
                date: "%s"
                url: "/%s"
                toc: true
                categories: [%s]
                tags: [%s]
                ---
                \n
                %s
                """.formatted(
                article.getTitle(),
                image,
                publishedDate,
                slug,
                categories,
                tags,
                content);
    }

    public static void writeArticleToMarkdownFile(Article article, boolean enhanced) {
        String slug = article.getSlug();
        if (slug.startsWith("/")) {
            slug = slug.substring(1);
        }

        String content = generateHugoArticle(article, enhanced);
        String filename = slug + ".md";
        Path path = Paths.get(filename);
        try {
            Files.writeString(path, content, UTF_8);
            log.info("Created blog file: {}", filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
