package org.acme.resource;


import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hal.HalEntityWrapper;
import io.quarkus.hal.HalLink;
import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;
import io.quarkus.resteasy.reactive.links.RestLinksProvider;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.MovieDto;
import org.acme.dto.SignupDto;
import org.acme.dto.UserDto;
import org.acme.dto.UserMovieDto;
import org.acme.exceptions.DuplicateResourceException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.model.User;
import org.acme.model.UserMovie;
import org.acme.service.AuthService;
import org.acme.service.UserService;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/users")
@RolesAllowed({"USER", "ADMIN"})
public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    private AuthService authService;



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
    @RestLink(rel = "self")      //the self link is not returned, If we return the UserDto object instead of the Response it appears
    @InjectRestLinks(RestLinkType.INSTANCE)
    @ResponseStatus(value= 200)
    public UserDto getById(@PathParam("id") Long id) throws ResourceNotFoundException {    //returns a a movie dto

        UserDto userDto=userService.retrieveUserById(id);
        //return Response.ok(userDto).build();
        return userDto;

    }

    @Transactional
    @DELETE                                        //delete user by id
    @RolesAllowed({"ADMIN"})    //only admins are allowed to delete users
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) throws ResourceNotFoundException {    //returns a a movie dto

        userService.deleteUserById(id);
        return Response.ok().build();


    }

    @Transactional
    @POST
    @Path("/{userId}/follows")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUserToFollow(@PathParam("userId") Long userId,String usernameToFollow) throws ResourceNotFoundException {

        userService.follow(userId,usernameToFollow);

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


    //user adds rate to movie
    @Transactional
    @PATCH
    @Path("/{userId}/movies/{movieId}/rate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response rateAMovie(@PathParam("userId") Long userId,@PathParam("movieId") Long movieId, @Valid int rate) throws ResourceNotFoundException {

        UserMovieDto userMoviedto=userService.addRateToMovie(userId,movieId,rate);
        System.out.println("rate is: "+userMoviedto.getRate());

        return Response.status(Response.Status.OK).entity(userMoviedto).build();

    }

    @Transactional
    @PATCH
    @Path("/{userId}/movies/{movieId}/review")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reviewAMovie(@PathParam("userId") Long userId,@PathParam("movieId") Long movieId, @Valid String review) throws ResourceNotFoundException {

        UserMovieDto userMoviedto=userService.addReviewToMovie(userId,movieId,review);
        //System.out.println("review is: "+userMoviedto.getReview());

        return Response.status(Response.Status.OK).entity(userMoviedto).build();

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
    //@Produces(MediaType.APPLICATION_JSON)
    public Response removeMovieFromUser(@PathParam("userId") Long userId, @PathParam("movieId") Long movieId) throws ResourceNotFoundException {

       userService.removeMovieFromUser(userId,movieId);
                                                                       //TODO: provide link to retrieve movies of user. Similarly to Sping HATEOAS
        return Response.ok(Response.Status.OK).build();
    }

    @GET
    @Path("/{userId}/followers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFollowers(@PathParam("userId") Long userId) throws ResourceNotFoundException { //retrieve people that these user follows

        Set<UserDto> userDtos=userService.getFollowersOfUser(userId);

        return Response.status(Response.Status.OK).entity(userDtos).build();
    }

    @GET
    @Path("/{userId}/follows")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFollows(@PathParam("userId") Long userId) throws ResourceNotFoundException { //retrieve people that these user follows

        Set<UserDto> userDtos=userService.getFollowingUsers(userId);

        return Response.status(Response.Status.OK).entity(userDtos).build();
    }


}