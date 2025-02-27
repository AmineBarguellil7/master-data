/**
 * Created By Amine Barguellil
 * Date : 3/4/2024
 * Time : 3:16 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresSQLExtension implements BeforeAllCallback {

    private PostgreSQLContainer<?> postgres;


    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        postgres = new PostgreSQLContainer("postgres:16")
                .withDatabaseName("masterdata")
                .withPassword("amine")
                .withUsername("postgres");
        postgres.start();

        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.password", postgres.getPassword());
        System.setProperty("spring.datasource.username", postgres.getUsername());
      System.setProperty("spring.flyway.table", "masterdata_version");

    }
}
