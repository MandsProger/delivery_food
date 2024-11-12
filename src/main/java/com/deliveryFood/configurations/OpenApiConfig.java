package com.deliveryFood.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(info = @Info(
        title = "My FOOD API",
        version = "1.0.0",
        description = "API food project",
        contact = @Contact(
                name = "Mattew",
                email = "matvei2005228@gmail.com",
                url = "https://github.com/MandsProger"
        ),
        license = @License(
                name = "Apache 2.0",
                url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
))
public class OpenApiConfig {
    // Дополнительные настройки, если нужны
}
