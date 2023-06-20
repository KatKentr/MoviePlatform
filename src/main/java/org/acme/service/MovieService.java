package org.acme.service;


import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.MovieDto;
import org.acme.exceptions.DuplicateResourceException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.mapper.MovieMapper;
import org.acme.model.Movie;
import org.acme.model.User;
import org.acme.model.UserMovie;
import org.acme.repository.MovieRepository;
import org.acme.repository.UserMovieRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@ApplicationScoped
public class MovieService {


    MovieRepository movieRepository;


    @Inject
    MovieMapper movieMapper;


    @Inject
    UserMovieRepository userMovieRepository;


    public MovieService(MovieRepository movieRepository){                //constructor-based injection
        this.movieRepository=movieRepository;
    }

    public List<MovieDto> retrieveAllMovies(@PositiveOrZero(message="pageNo should be a postive integer") int pageNo, @PositiveOrZero(message="pageSize should be a positive integer") int pageSize){     //return a list of MovieDtos, pageSize corresponds to ItemsPerPage //OPEN QUESTION: constraint to maximum number of pages?

        List<Movie> movies=movieRepository.findAll().page(Page.of(pageNo,pageSize)).list();
        List<MovieDto> moviesDto=movies.stream().map(m-> movieMapper.toDto(m)).collect(Collectors.toList());
        return moviesDto;


    }

    public MovieDto saveNewMovie(MovieDto movieDto) throws DuplicateResourceException {        //returns a MovieDto

        Optional<Movie> opt=movieRepository.findByTitle(movieDto.getTitle());

        if (!opt.isEmpty()){

            throw new DuplicateResourceException("movie with title "+movieDto.getTitle()+" already exists");
        }

        Movie movie=movieMapper.toEntity(movieDto);
            //movie.persist();
           movieRepository.persist(movie);

           //Question; How can we retrieve the newly created entity in quarkus?

          if (existsInDb(movie)){

              return movieMapper.toDto(movie);

          } else {

              throw new NotFoundException();
          }
    }


    public MovieDto retrieveMovieById(Long id) throws ResourceNotFoundException {

        Optional<Movie> optional=movieRepository.findByIdOptional(id);
        Movie movie=optional.orElseThrow(() -> new ResourceNotFoundException("Movie with id: "+id+" does not exist"));
        return movieMapper.toDto(movie);
    }

    public List<MovieDto> retrieveMoviesByCountry(String country){

        List<Movie> movies=movieRepository.findByCountry(country);
        List<MovieDto> moviesDto=movies.stream().map(m-> movieMapper.toDto(m)).collect(Collectors.toList());
        return moviesDto;
    }


    public List<MovieDto> retrieveMoviesByDirector(String director){

        List<Movie>  movies=movieRepository.findByDirector(director);
        List<MovieDto> moviesDto=movies.stream().map(m-> movieMapper.toDto(m)).collect(Collectors.toList());
        return moviesDto;
    }

    public MovieDto retrieveMovieByTitle(String title) throws ResourceNotFoundException {   //There could be movies with the same name, right?

        Optional<Movie> optional=movieRepository.findByTitle(title);
        Movie movie=optional.orElseThrow(() -> new ResourceNotFoundException("Movie with title: "+title+ " not found"));
        return movieMapper.toDto(movie);
    }



        public void deleteMovieById(Long id) throws ResourceNotFoundException {

        Optional<Movie> optional=movieRepository.findByIdOptional(id);
        Movie movie=optional.orElseThrow(() ->new ResourceNotFoundException("Movie with title: "+id+ " not found"));
//        List<UserMovie> users=movie.getUsers();
//        if (!users.isEmpty())   {   //if this movie is associated with users. We have to remove the association. This approach did not work though
//                users.stream().map(x -> x.getUser()).forEach(u -> {System.out.println(u.getUsername());u.getMovies().stream().forEach( m->System.out.println("before movie removal "+m.getMovie().getTitle()));u.removeMovie(movie);u.getMovies().stream().forEach( m->System.out.println("after movie removal "+m.getMovie().getTitle()));});
//            //users.stream().map(x -> x.getUser()).forEach(u -> {System.out.println(u.getUsername());u.removeMovie(movie);});
//        }

        userMovieRepository.deleteByMovie(movie);   //remove the user-movie associations for this movie
        movieRepository.deleteById(id);

    }

    public boolean existsInDb(Movie movie){

        return movieRepository.isPersistent(movie);
    }


















}
