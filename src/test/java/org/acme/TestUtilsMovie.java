package org.acme;

import org.acme.model.Movie;

import java.util.Optional;

public class TestUtilsMovie {


    public TestUtilsMovie(){}


    public Movie createValidMovie(String title,String director,String description, String country, Long id){

        Movie movie=new Movie();
        movie.setTitle("title");
        movie.setDirector("director");
        movie.setDescription("description");
        movie.setCountry("Country");
        Optional<Long> movieId= Optional.ofNullable(id);
        if (movieId.isPresent()) {
            movie.setId(movieId.get());}
        return movie;


    }




}
