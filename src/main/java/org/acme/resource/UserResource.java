package org.acme.resource;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.MovieDto;
import org.acme.dto.UserDto;
import org.acme.model.User;
import org.acme.service.UserService;

import java.util.List;

@Path("/users")
public class UserResource {

    @Inject
    private UserService userService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDto> users(){

        List<UserDto> usersDto=userService.retrieveAllUsers();
        return usersDto;
        //return Response.ok(movies).build();   //In this case, the return type of the method would be Response instead of List<Movie)
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUser(UserDto userDto) {        //returns a userDto

        UserDto newUserDto = userService.saveNewUser(userDto);
//          movieService.saveNewMovie();
//          if (movieService.existsInDb(movie)){
//              return Response.status(Status.CREATED).entity(movie).build();     //returns status 201 and the movie as json response
//          }
//          return Response.status(NOT_FOUND).build();

        return Response.status(Response.Status.CREATED).entity(newUserDto).build();

    }

}
