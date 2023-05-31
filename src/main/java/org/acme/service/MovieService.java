package org.acme.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.MovieDto;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.mapper.MovieMapper;
import org.acme.model.Movie;
import org.acme.repository.MovieRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//https://github.com/GiuseppeScaramuzzino/quarkus-hibernate-orm-panache-repository/blob/main/src/main/java/org/gs/MovieResource.java
//https://redhat-developer-demos.github.io/quarkus-tutorial/quarkus-tutorial/panache.html
//https://quarkus.io/guides/transaction

@ApplicationScoped
public class MovieService {


    MovieRepository movieRepository;


    @Inject
    MovieMapper movieMapper;


    public MovieService(MovieRepository movieRepository){                //Does constructor-based injection take place or should i replace with @Inject?
        this.movieRepository=movieRepository;
    }

    public List<MovieDto> retrieveAllMovies(){     //return a list of MovieDtos

        List<Movie> movies=movieRepository.listAll();
        List<MovieDto> moviesDto=movies.stream().map(m-> movieMapper.toDto(m)).collect(Collectors.toList());
        return moviesDto;


    }

    public MovieDto saveNewMovie(MovieDto movieDto){        //returns a MovieDto


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


    public boolean deleteMovieById(Long id){

      return movieRepository.deleteById(id);

       //QUESTION
       //or:
//        Movie entity=movieRepository.findById(id);
//        if (entity==null){
//
//            throw new NotFoundException();
//        }
//
//        movieRepository.delete(entity);

    }

    public boolean existsInDb(Movie movie){

        return movieRepository.isPersistent(movie);
    }


















}
