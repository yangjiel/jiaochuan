package com.jiaochuan.hazakura.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }
}
