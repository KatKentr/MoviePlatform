package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Movie;
import org.acme.model.User;
import org.acme.model.UserMovie;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class UserMovieRepository implements PanacheRepository<UserMovie> {


    public Optional<UserMovie> findByCompositePrimaryKey(UserMovie.UserMovieId pk){

        Optional<UserMovie> userMovie=find("primaryKey",pk).firstResultOptional();

        return userMovie;
    }

    public List<UserMovie> findByUser(User user){   //retrieve all movies that the user has selected

        List<UserMovie> userMovies=list("user",user);

        return userMovies;
    }


    public void deleteByMovie(Movie movie){

        delete("movie",movie);

    }
}
