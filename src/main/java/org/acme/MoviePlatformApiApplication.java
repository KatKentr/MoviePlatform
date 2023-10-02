package org.acme;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

//OpenAPI: defines a standard standard, language-agnostic interface to RESTful APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.
//This Jakarta REST Application class was created to provide global API information by using OpenAPI annotations
//Check the APIs operations and schemas http://localhost:8080/q/swagger-ui/
@OpenAPIDefinition(
        tags = {
                @Tag(name="movies", description="Movie operations"),
                @Tag(name="users", description="User operations")
        },
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
