package com.sivalabs.blogify;

import org.springframework.boot.SpringApplication;

public class TestBlogifyApplication {

    public static void main(String[] args) {
        System.setProperty("spring.docker.compose.enabled", "false");

        SpringApplication
                .from(BlogifyApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
