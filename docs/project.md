# Project: Blogify

## Mission
Blogify is an AI-assisted blog authoring tool that generates, enhances, evaluates, and publishes blog posts to GitHub using Spring AI. It is a personal/developer tool with no multi-user authentication requirements.

## Tech Stack
- Language: Java 25
- Framework: Spring Boot 4.0.5
- Build tool: Maven
- Database: PostgreSQL
- ORM: Spring Data JPA (Hibernate)
- Migrations: Flyway
- Messaging: none
- Testing: JUnit 5 (spring-boot-starter-webmvc-test), Testcontainers (PostgreSQL)
- Other:
  - Spring AI 2.0.0-M4 (OpenAI model, pgvector vector store, JDBC chat memory repository, MCP client WebFlux)
  - Thymeleaf + thymeleaf-layout-dialect (server-side templating)
  - HTMX 2.0.4 + htmx-spring-boot-thymeleaf 5.0.0 (partial page updates)
  - Bootstrap 5.3.8, jQuery 3.7.1, Font Awesome 7.0.1 (via WebJars)
  - CommonMark 0.27.0 (Markdown → HTML rendering)
  - Spring Boot Actuator, Spring Validation
  - Spring Boot DevTools, Spring Boot Docker Compose

## Architecture
Simple Layered Architecture. All code lives under `com.sivalabs.blogify`, split into four packages:

```
com.sivalabs.blogify.web       — Spring MVC @Controller classes and form/request records
com.sivalabs.blogify.agents    — Spring AI interaction: ChatClient calls, prompt templates, response types
com.sivalabs.blogify.domain    — JPA @Entity, Spring Data repositories, and domain utilities
com.sivalabs.blogify.config    — Spring @Configuration beans (AI clients, etc.)
```

The `web` layer depends on `agents` and `domain`. The `agents` layer depends on `domain`. The `domain` layer has no upward dependencies.

## Conventions
- Package naming: `com.sivalabs.blogify.<layer>` (web / agents / domain / config)
- Views: Thymeleaf templates, no REST API — controllers return view names or redirect strings
- HTMX: Partial-update endpoints are annotated `@HxRequest` and return a fragment view name
- REST base path: none (plain paths — `/`, `/articles`, `/articles/{id}`, `/create-article`, etc.)
- Error handling: no `GlobalExceptionHandler` yet; controllers use `orElseThrow()` directly
- Authentication: none — all routes are publicly accessible
- AI clients: two named `ChatClient` beans — `openAiChatClient` (content generation/enhancement/publishing) and `verifierChatClient` (configurable provider for evaluation/fact-checking). Always inject by `@Qualifier`
- Prompt files: stored as Markdown resources under `src/main/resources/prompts/` and referenced via `ApplicationProperties`
- GitHub publishing: the `ArticleAgent.publishArticle` method uses the MCP GitHub tool to create files in the repo/directory configured via `app.github-repo` and `app.github-repo-content-dir`

## Approved Dependencies
- Spring Boot 4.0.5 starters (web MVC, data JPA, validation, actuator, thymeleaf, flyway, devtools, docker-compose)
- Spring AI 2.0.0-M4 (openai model, pgvector, jdbc chat memory, mcp-client-webflux, advisors-vector-store)
- PostgreSQL driver + Flyway PostgreSQL dialect
- Thymeleaf Layout Dialect, htmx-spring-boot-thymeleaf 5.0.0
- WebJars: bootstrap 5.3.8, htmx.org 2.0.4, jquery 3.7.1, font-awesome 7.0.1, webjars-locator-lite
- CommonMark 0.27.0
- commons-io 1.3.2
- Testcontainers (JUnit Jupiter + PostgreSQL)
- netty-resolver-dns-native-macos (macOS ARM DNS workaround)

Any dependency outside this list should be flagged and confirmed before adding.
