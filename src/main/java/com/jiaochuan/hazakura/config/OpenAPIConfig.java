package com.jiaochuan.hazakura.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;

public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPIInfo() {
        return new OpenAPI()
                .info(new Info().title("交川App API描述文档"))
                .addServersItem(new Server().url("http://106.13.70.236/swagger-ui.html"));
    }
}
