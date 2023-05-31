package org.acme.resource;


import io.quarkus.hal.HalEntityWrapper;
import io.quarkus.hal.HalLink;
import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;
import io.quarkus.resteasy.reactive.links.RestLinksProvider;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.MovieDto;
import org.acme.dto.UserDto;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.model.User;
import org.acme.service.UserService;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/users")
public class UserResource {

    @Inject
    private UserService userService;



    @GET
    @Produces({ MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON })
    @RestLink(rel = "all-users")           //declare the web links that will be returned
    @InjectRestLinks                     //injecting web links into the response HTTP headers
    public List<UserDto> users() {

        List<UserDto> usersDto = userService.retrieveAllUsers();
        return usersDto;
        //return Response.ok(movies).build();   //In this case, the return type of the method would be Response instead of List<Movie)
    }


    @GET                                        //retrieve user by id
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON })
    @RestLink(rel = "self")      //the self link is not returned. If we return the UserDto object instead of the Response it may appear
    @InjectRestLinks(RestLinkType.INSTANCE)
    public Response getById(@PathParam("id") Long id) throws ResourceNotFoundException {    //returns a a movie dto

        UserDto userDto=userService.retrieveUserById(id);
        return Response.ok(userDto).build();


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


    @Transactional
    @POST
    @Path("/{userId}/follows")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUserToFollow(@PathParam("userId") Long userId,User userToFollow) throws ResourceNotFoundException {

        userService.follow(userId,userToFollow);

        return Response.status(Response.Status.CREATED).build();
    }

    @Transactional
    @DELETE
    @Path("/{userId}/follows/{userIdToUnfollow}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowUser(@PathParam("userId") Long userId,@PathParam("userIdToUnfollow")Long userIdToUnfollow) throws ResourceNotFoundException {

        userService.unfollow(userId,userIdToUnfollow);

        return Response.status(Response.Status.OK).build();
    }




    @Transactional
    @POST
    @Path("/{userId}/movies")     //add a movie to a user
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieToUser(@PathParam("userId") Long userId,@Valid MovieDto movieDto) throws ResourceNotFoundException {

        userService.addMovieToUser(userId,movieDto);

        return Response.status(Response.Status.CREATED).build();
    }


    @GET
    @Path("/{userId}/movies")
    @Consumes(MediaType.APPLICATION_JSON)      //get the movies of a specific user
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveMoviesOfUser(@PathParam("userId") Long userId) throws ResourceNotFoundException {

        //to implement
        List<MovieDto> movieDtos=userService.getMoviesOfUser(userId);

        return Response.ok(movieDtos).build();
    }

    @Transactional
    @DELETE
    @Path("/{userId}/movies/{movieId}")
    @Consumes(MediaType.APPLICATION_JSON)      //remove a movie form
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeMovieFromUser(@PathParam("userId") Long userId, @PathParam("movieId") Long movieId) throws ResourceNotFoundException {

       userService.removeMovieFromUser(userId,movieId);
                                                                       //TODO: provide link to retrieve movies of user. Similarly to Sping HATEOAS
        return Response.ok(Response.Status.OK).build();
    }








    @GET
    @Path("/{userId}/followers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFollowers(@PathParam("userId") Long userId) { //retrieve people that these user follows

        Set<User> users=userService.getFollowers(userId);

        return Response.status(Response.Status.OK).entity(users).build();
    }

    @GET
    @Path("/{userId}/follows")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFollows(@PathParam("userId") Long userId) { //retrieve people that these user follows

        Set<User> users=userService.getFollowing(userId);

        return Response.status(Response.Status.OK).entity(users).build();
    }


}