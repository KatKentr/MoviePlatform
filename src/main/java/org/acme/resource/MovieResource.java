package org.acme.resource;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.MovieDto;
import org.acme.exceptions.DuplicateResourceException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.service.MovieService;

import java.util.List;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;


@Path("/movies")
public class MovieResource {

    @Inject
    private MovieService movieService;               //TODO: try also constructor-based injection


//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Movie> movies(){
//
//        return Movie.listAll();            //listAll() method comes from Panache
//        //List<Movie> movies=Movie.listAll();
//        //return Response.ok(movies).build();   //In this case, the return type of the method would be Response instead of List<Movie)
//    }


    //example of a GET REST endpoint to use a QueryParam curl localhost:8080/movies?director=Fatih Akim
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MovieDto> movies(@QueryParam("director") String director){
        if (director!=null){
            return movieService.retrieveMoviesByDirector(director);
        }

        return movieService.retrieveAllMovies();            //listAll() method comes from Panache
        //List<Movie> movies=Movie.listAll();
        //return Response.ok(movies).build();   //In this case, the return type of the method would be Response instead of List<Movie)
    }


   //create a new movie
    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newMovie(@Valid MovieDto movieDto) throws DuplicateResourceException {        //returns a movieDto

          MovieDto newMovieDto=movieService.saveNewMovie(movieDto);
//          movieService.saveNewMovie();
//          if (movieService.existsInDb(movie)){
//              return Response.status(Status.CREATED).entity(movie).build();     //returns status 201 and the movie as json response
//          }
//          return Response.status(NOT_FOUND).build();

        return Response.status(Response.Status.CREATED).entity(newMovieDto).build();
    }
//TO DO: We have a @NotNull validation for the attributes in the entity clas. In case of null, application exits. Handle this situation

    //retrieve movie by title

    @GET
    @Path("/title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public MovieDto getByTitle(@PathParam("title") String title) throws ResourceNotFoundException {

        MovieDto movieDto=movieService.retrieveMovieByTitle(title);

        return movieDto;

    }

    @GET
    @Path("/country/{country}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByCountry(@PathParam("country") String country){

        List<MovieDto> moviesDto=movieService.retrieveMoviesByCountry(country);
        return Response.ok(moviesDto).build();

    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) throws ResourceNotFoundException {    //returns a a movie dto

       MovieDto movieDto=movieService.retrieveMovieById(id);
        return Response.ok(movieDto).build();

    }


    @Transactional
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") Long id){

        Boolean deleted=movieService.deleteMovieById(id);
        return deleted ? Response.noContent().build() : Response.status(BAD_REQUEST).build();

    }

















}
