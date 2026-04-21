# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package -DskipTests

# Run (requires Docker for PostgreSQL via Spring Boot Docker Compose)
./mvnw spring-boot:run

# Run all tests (uses Testcontainers — Docker must be running)
./mvnw test

# Run a single test class
./mvnw test -Dtest=BlogifyApplicationTests

# Java/Maven versions (managed via SDKMAN)
# java=25-tem, maven=3.9.15
```
