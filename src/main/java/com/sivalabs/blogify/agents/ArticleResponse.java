package com.sivalabs.blogify.agents;

import java.util.List;
import java.util.Set;

public record ArticleResponse(
            String title,
            String content,
            String slug,
            List<String> images,
            Set<String> categories,
            Set<String> tags
    ) {
    }