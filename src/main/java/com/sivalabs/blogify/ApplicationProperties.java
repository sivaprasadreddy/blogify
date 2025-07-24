package com.sivalabs.blogify;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private Resource createBlogPrompt;
    private Resource enhanceBlogPrompt;
    private Resource evaluateBlogPrompt;
    private String verifierBaseUrl;
    private String verifierApiKey;
    private String verifierModel;
    private String verifierCompletionsPath;
    private String githubRepo;
    private String githubRepoContentDir;

    public Resource getCreateBlogPrompt() {
        return createBlogPrompt;
    }

    public void setCreateBlogPrompt(Resource createBlogPrompt) {
        this.createBlogPrompt = createBlogPrompt;
    }

    public Resource getEnhanceBlogPrompt() {
        return enhanceBlogPrompt;
    }

    public void setEnhanceBlogPrompt(Resource enhanceBlogPrompt) {
        this.enhanceBlogPrompt = enhanceBlogPrompt;
    }

    public Resource getEvaluateBlogPrompt() {
        return evaluateBlogPrompt;
    }

    public void setEvaluateBlogPrompt(Resource evaluateBlogPrompt) {
        this.evaluateBlogPrompt = evaluateBlogPrompt;
    }

    public String getVerifierBaseUrl() {
        return verifierBaseUrl;
    }

    public void setVerifierBaseUrl(String verifierBaseUrl) {
        this.verifierBaseUrl = verifierBaseUrl;
    }

    public String getVerifierApiKey() {
        return verifierApiKey;
    }

    public void setVerifierApiKey(String verifierApiKey) {
        this.verifierApiKey = verifierApiKey;
    }

    public String getVerifierModel() {
        return verifierModel;
    }

    public void setVerifierModel(String verifierModel) {
        this.verifierModel = verifierModel;
    }

    public String getVerifierCompletionsPath() {
        return verifierCompletionsPath;
    }

    public void setVerifierCompletionsPath(String verifierCompletionsPath) {
        this.verifierCompletionsPath = verifierCompletionsPath;
    }

    public String getGithubRepo() {
        return githubRepo;
    }

    public void setGithubRepo(String githubRepo) {
        this.githubRepo = githubRepo;
    }

    public String getGithubRepoContentDir() {
        return githubRepoContentDir;
    }

    public void setGithubRepoContentDir(String githubRepoContentDir) {
        this.githubRepoContentDir = githubRepoContentDir;
    }
}
