package com.sivalabs.blogify;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private String verifierBaseUrl;
    private String verifierApiKey;
    private String verifierModel;
    private String verifierCompletionsPath;
    private String githubRepo;
    private String githubRepoContentDir;

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
