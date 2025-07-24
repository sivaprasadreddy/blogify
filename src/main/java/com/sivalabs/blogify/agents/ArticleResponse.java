package com.sivalabs.blogify.agents;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record ArticleResponse(
            String title,
            String content,
            String slug,
            LocalDateTime date,
            List<String> images,
            Set<String> categories,
            Set<String> tags
    ) {
    }