package com.sivalabs.blogify.domain;

import java.util.List;

public record EvaluationResponse(String summary, List<String> issues) {
}