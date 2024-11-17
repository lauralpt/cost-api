package com.etraveli.cardcostapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Clearing Cost API", version = "1.0", description = "API for clearing cost management"))
public class OpenApiConfig {
}
