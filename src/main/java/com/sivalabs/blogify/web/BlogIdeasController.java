package com.sivalabs.blogify.web;

import com.sivalabs.blogify.domain.ArticleService;
import com.sivalabs.blogify.domain.Topic;
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
    private final ArticleService articleService;

    BlogIdeasController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/generate-ideas")
    String showGenerateIdeasPage(Model model) {
        model.addAttribute("topic", new Topic("", "", 3));
        return "generate-ideas";
    }

    @PostMapping("/generate-ideas")
    String showGeneratedIdeas(@ModelAttribute("topic") Topic topic, Model model) {
        log.info("Generating ideas on topic: '{}' for '{}' audience", topic.subject(), topic.audience());
        var ideas = articleService.generateIdeas(topic);
        model.addAttribute("ideas", ideas);
        return "generate-ideas";
    }
}
