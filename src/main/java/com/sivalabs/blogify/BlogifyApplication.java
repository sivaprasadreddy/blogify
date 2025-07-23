package com.sivalabs.blogify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BlogifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogifyApplication.class, args);
    }

}
