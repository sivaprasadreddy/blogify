package com.sivalabs.blogify.config;

import com.sivalabs.blogify.ApplicationProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AIConfig {
    private final ApplicationProperties properties;

    AIConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Qualifier("generatorChatClient")
    ChatClient generatorChatClient(OllamaChatModel chatModel,
                                ToolCallbackProvider tools,
                                ChatMemory chatMemory) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        return builder
                //.defaultSystem(createBlogPrompt)
                .defaultAdvisors(
                   // MessageChatMemoryAdvisor.builder(chatMemory).build(),
                    SimpleLoggerAdvisor.builder().build()
                )
                .defaultToolCallbacks(tools)
                .build();
    }

    @Bean
    @Qualifier("verifierChatClient")
    ChatClient verifierChatClient(OllamaChatModel chatModel,
                                ToolCallbackProvider tools,
                                ChatMemory chatMemory) {
        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl(properties.getVerifierBaseUrl())
                .build();
        OllamaOptions defaultOptions = OllamaOptions.builder()
                    .model(properties.getVerifierModel())
                    .temperature(0.7)
                    .build();

        OllamaChatModel verifierChatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(defaultOptions)
                .build();

        ChatClient.Builder builder = ChatClient.builder(verifierChatModel);
        return builder
                //.defaultSystem(evaluateBlogPrompt)
                .defaultAdvisors(
                   // MessageChatMemoryAdvisor.builder(chatMemory).build(),
                    SimpleLoggerAdvisor.builder().build()
                )
                //.defaultToolCallbacks(tools)
                .build();
    }
}
