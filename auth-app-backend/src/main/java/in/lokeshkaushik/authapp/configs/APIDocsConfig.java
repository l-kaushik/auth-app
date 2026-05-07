package in.lokeshkaushik.authapp.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Auth Application",
                description = "Generic auth app that can be used with any application",
                contact = @Contact(
                        name = "Lokesh Kaushik",
                        url = "https://lokeshkaushik.in",
                        email = "contact.lokeshkaushik@gmail.com"
                ),
                version = "1.0"
        ),
        security = {
                @SecurityRequirement(
                        name="bearerAuth"
                )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer", // Authorization: Bearer token
        bearerFormat = "JWT"
)
public class APIDocsConfig {

}
