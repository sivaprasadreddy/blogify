package com.sivalabs.blogify.agents;

import java.util.List;

public record ArticleIdeas(List<ArticleIdea> ideas) {}

record ArticleIdea(String title, String briefDescription) {}