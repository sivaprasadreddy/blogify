package com.sivalabs.blogify.domain;

import com.sivalabs.blogify.ApplicationProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ChatClient openAiChatClient;
    private final ChatClient verifierChatClient;
    private final ApplicationProperties properties;
    private final Resource createBlogPrompt;
    private final Resource enhanceBlogPrompt;
    private final Resource evaluateBlogPrompt;

    public ArticleService(ArticleRepository articleRepository,
                          @Qualifier("openAiChatClient") ChatClient openAiChatClient,
                          @Qualifier("verifierChatClient") ChatClient verifierChatClient, ApplicationProperties properties
    ) {
        this.articleRepository = articleRepository;
        this.openAiChatClient = openAiChatClient;
        this.verifierChatClient = verifierChatClient;
        this.properties = properties;
        this.createBlogPrompt = new ClassPathResource("/prompts/create-blog-prompt.md");
        this.enhanceBlogPrompt = new ClassPathResource("/prompts/enhance-blog-prompt.md");
        this.evaluateBlogPrompt = new ClassPathResource("/prompts/evaluate-blog-prompt.md");
    }

    public List<Idea> generateIdeas(Topic topic) {
        PromptTemplate promptTemplate = new PromptTemplate(
                """
                Generate {count} blog post titles with brief description of what will be covered in that post
                for {audience} audience on the following subject:
                
                {subject}
                
                Keep the title short and intriguing.
                """
        );
        Map<String, Object> params = Map.of(
                "count", topic.count(),
                "subject", topic.subject(),
                "audience", topic.audience()
        );
        Prompt prompt = promptTemplate.create(params);
        return openAiChatClient.prompt(prompt).call()
                .entity(new ParameterizedTypeReference<>() {});
    }

    public ArticleResponse generateArticle(ArticleRequest request) {
        return openAiChatClient.prompt()
                .system(createBlogPrompt)
                .user("Topic is :" + request.topic())
                .call().
                entity(ArticleResponse.class);
    }

    public EvaluationResponse evaluateArticle(Long id, boolean enhanced) {
        Article article = articleRepository.findById(id).orElseThrow();
        String content = enhanced ? article.getEnhancedContent() : article.getContent();
        if (content == null) {
            throw new IllegalArgumentException("Content is required");
        }
        return verifierChatClient.prompt()
                .system(evaluateBlogPrompt)
                .user(p -> p
                        .text("""
                                Evaluate, fact check and provide
                                a summary and the list of issues found
                                in the following article content:
                                
                                {content}
                                """)
                        .param("content", content))
                .call().
                entity(EvaluationResponse.class);
    }

    public EnhanceResponse enhanceArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        return openAiChatClient.prompt()
                .system(enhanceBlogPrompt)
                .user(p -> p
                        .text("""
                                Enhance the following article content:
                                
                                {content}
                                """)
                        .param("content", article.getContent()))
                .call().
                entity(EnhanceResponse.class);
    }

    public String publishArticle(Long id, boolean enhanced) {
        Article article = articleRepository.findById(id).orElseThrow();
        String articleContent = ArticleUtils.generateHugoArticle(article, enhanced);
        return openAiChatClient.prompt()
            .system(p -> p.text("You are a helper to publish article content to github"))
            .user(p -> p
                .text("""
                        Create a file with name {filename} inside the directory {directory} in my github repository {repo}
                        with the following content:
                        
                        {content}
                        """)
                .param("filename", article.getSlug()+".md")
                .param("directory", properties.getGithubRepoContentDir())
                .param("repo", properties.getGithubRepo())
                .param("content", articleContent)
            )
            .call()
            .content();
    }
}
