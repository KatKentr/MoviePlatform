package org.acme;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

//This Jakarta REST Application class was created to provide global API information by using OpenAPI annotations

@OpenAPIDefinition(
        info = @Info(
                title="Movie Platform API",
                version = "1.0.0",
                contact = @Contact(
                        name = "Aikaterini Kentroti",
                        url = "http://localhost:8080"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class MoviePlatformApiApplication extends Application {
}
