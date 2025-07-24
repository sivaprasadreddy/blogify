package com.sivalabs.blogify.agents;

import java.util.List;

public record EvaluateArticleResponse(String summary, List<String> issues) {
}