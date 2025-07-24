package com.sivalabs.blogify.web;

import com.sivalabs.blogify.agents.ArticleAgent;
import com.sivalabs.blogify.agents.ArticleIdeasRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class BlogIdeasController {
    private static final Logger log = LoggerFactory.getLogger(BlogIdeasController.class);
    private final ArticleAgent articleAgent;

    BlogIdeasController(ArticleAgent articleAgent) {
        this.articleAgent = articleAgent;
    }

    @GetMapping("/generate-ideas")
    String showGenerateIdeasPage(Model model) {
        model.addAttribute("articleIdeasRequest", new ArticleIdeasRequest("", "", 3));
        return "generate-ideas";
    }

    @PostMapping("/generate-ideas")
    String showGeneratedIdeas(@ModelAttribute("articleIdeasRequest") ArticleIdeasRequest articleIdeasRequest, Model model) {
        log.info("Generating ideas on articleIdeasRequest: '{}' for '{}' audience", articleIdeasRequest.subject(), articleIdeasRequest.audience());
        var ideas = articleAgent.generateIdeas(articleIdeasRequest);
        model.addAttribute("ideas", ideas);
        return "generate-ideas";
    }
}
