package org.acme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;



@Entity
@Table(name="movies")
public class Movie  {      //We are not creating getters and setters and we are not providing an @Id: Panache feature
                                                 //By extending PanacheEntity, we’re using the Active Record persistence pattern instead of a DAO. This means that all persistence methods are blended with our own Entity.
  @Id
  @GeneratedValue
   private Long id;

   @NotNull
   private String title;                          //Notice that we are setting the instance fields to public with Panache!

   @NotNull
   private String description;

   @NotNull
   private String director;

   @NotNull
   private String country;


   //we want to get the users who like a movie. Bidirectional Many-to-Many relationship
    @ManyToMany(mappedBy = "movies")
    //The property mappedBy has the value movies which represents the property movies in the file User.java.
    @JsonIgnore
    private Set<User> users;


    public Set<User> getUsers() { //Do we also need a setter?
        return users;
    }

    public Movie(){

   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getDirector() {
      return director;
   }

   public void setDirector(String director) {
      this.director = director;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }




}
