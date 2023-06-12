package org.acme.resource;


import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLinkType;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.SignupDto;
import org.acme.dto.UserDto;
import org.acme.exceptions.DuplicateResourceException;
import org.acme.service.AuthService;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

@Path("/auth")
@PermitAll
public class AuthResource {

    @Inject
    AuthService authService;


    @Transactional
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    @InjectRestLinks(RestLinkType.INSTANCE)   //injecting the URI for the created resource and the link to retrieve all-users into the response HTTP headers
    public Response newUser(@Valid SignupDto signupDto) throws DuplicateResourceException {        //returns a userDto

        UserDto newUserDto = authService.saveNewUser(signupDto);
//          movieService.saveNewMovie();
//          if (movieService.existsInDb(movie)){
//              return Response.status(Status.CREATED).entity(movie).build();     //returns status 201 and the movie as json response
//          }
//          return Response.status(NOT_FOUND).build();

        return Response.status(Response.Status.CREATED).entity(newUserDto).build();


    }



}
