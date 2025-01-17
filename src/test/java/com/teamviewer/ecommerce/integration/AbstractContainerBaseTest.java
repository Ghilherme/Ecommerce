package com.teamviewer.ecommerce.integration;

import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractContainerBaseTest {
    static final PostgreSQLContainer<?> postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:13-alpine");
        postgresContainer.start();
    }
}
