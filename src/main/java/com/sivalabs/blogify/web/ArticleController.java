package com.sivalabs.blogify.web;

import com.sivalabs.blogify.domain.Article;
import com.sivalabs.blogify.domain.ArticleRepository;
import com.sivalabs.blogify.domain.ArticleRequest;
import com.sivalabs.blogify.domain.ArticleResponse;
import com.sivalabs.blogify.domain.ArticleService;
import com.sivalabs.blogify.domain.EnhanceResponse;
import com.sivalabs.blogify.domain.EvaluationResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.OffsetDateTime;

@Controller
class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    ArticleController(ArticleRepository articleRepository, ArticleService articleService) {
        this.articleRepository = articleRepository;
        this.articleService = articleService;
    }

    @GetMapping("/create-article")
    String showCreateArticlePage(Model model) {
        model.addAttribute("request", new ArticleRequest("", ""));
        return "create-article";
    }

    @PostMapping("/create-article")
    String createArticle(ArticleRequest request, Model model) {
        ArticleResponse response = articleService.generateArticle(request);
        log.info("Generated blog content: {}", response);
        Article article = saveArticle(request, response);
        //ArticleUtils.writeArticleToMarkdownFile(article);
        return "redirect:/articles/"+article.getId();
    }

    @GetMapping("/articles")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "articles";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id, Model model) {
        Article article = articleRepository.findById(id).orElseThrow();
        model.addAttribute("article", ArticleForm.from(article));
        String articleContentHtml = MarkdownHelper.toHTML(article.getContent());
        model.addAttribute("articleContentHtml", articleContentHtml);

        String enhancedArticleContentHtml = "";
        String enhancedArticleContent = article.getEnhancedContent();
        if (enhancedArticleContent != null) {
            enhancedArticleContentHtml = MarkdownHelper.toHTML(enhancedArticleContent);
        }
        model.addAttribute("enhancedArticleContentHtml", enhancedArticleContentHtml);
        return "review-article";
    }

    @PostMapping("/update-article")
    String updateArticle(@ModelAttribute("article") @Valid ArticleForm articleForm,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            return "review-article";
        }
        Article article = articleRepository.findById(articleForm.id()).orElseThrow();
        article.setTitle(articleForm.title());
        article.setSlug(articleForm.slug());
        article.setCategories(articleForm.categories());
        article.setTags(articleForm.tags());
        article.setContent(articleForm.content());
        article.setEnhancedContent(articleForm.enhancedContent());

        articleRepository.save(article);
        return "redirect:/articles/"+article.getId();
    }

    @GetMapping("/articles/{id}/evaluate")
    @HxRequest
    public String evaluateArticle(@PathVariable Long id,
                                  @RequestParam(name = "enhanced", defaultValue = "false") boolean enhanced,
                                  Model model) {
        EvaluationResponse evaluationResponse = articleService.evaluateArticle(id, enhanced);
        System.out.println("evaluationResponse = " + evaluationResponse);
        String evaluationResponseHtml = MarkdownHelper.toHTML(evaluationResponse.summary());
        model.addAttribute("evaluationResponse", evaluationResponse);
        model.addAttribute("evaluationResponseHtml", evaluationResponseHtml);
        return "evaluation-result";
    }

    @GetMapping("/articles/{id}/enhance")
    public String enhanceArticle(@PathVariable Long id, Model model) {
        EnhanceResponse enhanceResponse = articleService.enhanceArticle(id);
        System.out.println("enhanceResponse = " + enhanceResponse);
        Article article = articleRepository.findById(id).orElseThrow();
        article.setEnhancedContent(enhanceResponse.content());
        articleRepository.save(article);
        return "redirect:/articles/"+id;
    }

    @PostMapping("/articles/{id}/publish")
    @ResponseBody
    @HxRequest
    public String publishArticle(@PathVariable Long id,
                                 @RequestParam(name = "enhanced", defaultValue = "false") boolean enhanced,
                                 Model model) {
        String publishResponse = articleService.publishArticle(id, enhanced);
        System.out.println("publishResponse = " + publishResponse);
        return "success";
    }

    private Article saveArticle(ArticleRequest request, ArticleResponse response) {
        Article article = new Article();
        article.setPrompt(request.topic());
        article.setTitle(response.title());
        article.setContent(response.content());
        article.setSlug(response.slug());
        article.setImages(response.images());
        article.setCategories(response.categories());
        article.setTags(response.tags());
        article.setCreatedAt(OffsetDateTime.now());

        return articleRepository.save(article);
    }
}
