package org.acme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



@Entity
@Table(name="movies")
public class Movie  {      //We are not creating getters and setters and we are not providing an @Id: Panache feature
                                                 //By extending PanacheEntity, we’re using the Active Record persistence pattern instead of a DAO. This means that all persistence methods are blended with our own Entity.
  @Id
  @GeneratedValue                  //entity identifier
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
//    @ManyToMany(mappedBy = "movies")
//    //The property mappedBy has the value movies which represents the property movies in the file User.java.
//    @JsonIgnore
//    private Set<User> users;
//
//
//    public Set<User> getUsers() { //Do we also need a setter?
//        return users;
//    }

    //According to: https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/

    @OneToMany(
            mappedBy = "movie"
//            cascade = CascadeType.ALL,   //do we need this?
//            orphanRemoval = true
    )
    private List<UserMovie> users=new ArrayList<>();



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



   //getters and setters for the list of UserMovie instances

    public List<UserMovie> getUsers() {
        return users;
    }

    public void setUsers(List<UserMovie> users) {
        this.users = users;
    }
}
