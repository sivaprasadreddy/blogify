package com.sivalabs.blogify.config;

import com.sivalabs.blogify.ApplicationProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
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
    @Qualifier("openAiChatClient")
    ChatClient openAiChatClient(OpenAiChatModel chatModel,
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
    ChatClient verifierChatClient(OpenAiChatModel chatModel,
                                ToolCallbackProvider tools,
                                ChatMemory chatMemory) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(properties.getVerifierBaseUrl())
                .apiKey(properties.getVerifierApiKey())
                .completionsPath(properties.getVerifierCompletionsPath())
                .build();
        OpenAiChatOptions defaultOptions = OpenAiChatOptions.builder()
                    .model(properties.getVerifierModel())
                    .temperature(0.7)
                    .build();

        OpenAiChatModel verifierChatModel = chatModel.mutate()
                .openAiApi(openAiApi)
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
