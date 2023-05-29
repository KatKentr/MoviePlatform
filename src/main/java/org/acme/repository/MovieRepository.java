package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Movie;

import java.util.List;
import java.util.Optional;

//Thought: support pagination? The process of diving a large dataset into smaller chunks(pages)
@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {

    public List<Movie> findByDirector(String director){  //now we can make a case-insensitive search for movies belonging to the same director

        return find("upper(director)",director.toUpperCase()).list();
    }

    //custom finder method

   public Optional<Movie> findByTitle(String title){
      return find("title",title).firstResultOptional();
   }

   public  List<Movie> findByCountry(String country){
      List<Movie> movies=list("SELECT m from Movie m WHERE m.country= ?1 ORDER BY id DESC",country);  //we write Movie(the name of the entity) not the name of the table (i.e movie)
      return movies;
   }


}
