package com.sivalabs.blogify.domain;

import java.util.List;

public record EvaluateArticleResponse(String summary, List<String> issues) {
}