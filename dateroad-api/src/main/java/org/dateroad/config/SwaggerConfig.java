package org.dateroad.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("DateRoad API Docs")
                .description("34기 앱잼팀 'DateRoad' API 명세서입니다.")
                .version("v1.0");
    }
}
