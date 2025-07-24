package com.sivalabs.blogify.agents;

import com.sivalabs.blogify.ApplicationProperties;
import com.sivalabs.blogify.domain.Article;
import com.sivalabs.blogify.domain.ArticleUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleAgent {
    private final ChatClient openAiChatClient;
    private final ChatClient verifierChatClient;
    private final ApplicationProperties properties;

    public ArticleAgent(@Qualifier("openAiChatClient") ChatClient openAiChatClient,
                        @Qualifier("verifierChatClient") ChatClient verifierChatClient, ApplicationProperties properties
    ) {
        this.openAiChatClient = openAiChatClient;
        this.verifierChatClient = verifierChatClient;
        this.properties = properties;
    }

    public ArticleIdeas generateIdeas(ArticleIdeasRequest articleIdeasRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(
                """
                Generate {count} blog post titles with brief description of what will be covered in that post
                for {audience} audience on the following subject:
                
                {subject}
                
                Keep the title short and intriguing.
                """
        );
        Map<String, Object> params = Map.of(
                "count", articleIdeasRequest.count(),
                "subject", articleIdeasRequest.subject(),
                "audience", articleIdeasRequest.audience()
        );
        Prompt prompt = promptTemplate.create(params);
        return openAiChatClient.prompt(prompt)
                .call()
                .entity(ArticleIdeas.class);
    }

    public ArticleResponse generateArticle(GenerateArticleRequest request) {
        return openAiChatClient.prompt()
                .system(properties.getCreateBlogPrompt())
                .user(u -> u.text("""
                        Generate an article for the following request:
                        
                        {topic}
                        """)
                        .param("topic", request.topic()))
                .call().
                entity(ArticleResponse.class);
    }

    public EvaluateArticleResponse evaluateArticle(Article article, boolean enhanced) {
        String content = enhanced ? article.getEnhancedContent() : article.getContent();
        if (content == null) {
            throw new IllegalArgumentException("Content is required");
        }
        return verifierChatClient.prompt()
                .system(properties.getEvaluateBlogPrompt())
                .user(p -> p
                        .text("""
                                Evaluate, fact check and provide
                                a summary and the list of issues found
                                in the following article content:
                                
                                {content}
                                """)
                        .param("content", content))
                .call().
                entity(EvaluateArticleResponse.class);
    }

    public EnhanceArticleResponse enhanceArticle(Article article) {
        return openAiChatClient.prompt()
                .system(properties.getEnhanceBlogPrompt())
                .user(p -> p
                        .text("""
                                Enhance the following article content:
                                
                                {content}
                                """)
                        .param("content", article.getContent()))
                .call().
                entity(EnhanceArticleResponse.class);
    }

    public String publishArticle(Article article, boolean enhanced) {
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
