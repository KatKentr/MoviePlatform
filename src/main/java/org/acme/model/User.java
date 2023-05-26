package org.acme.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;


//A user can select their favorite movies
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //we want to retrieve movies like by user
 //!Remark: By putting cascade={CascadeType.PERSIST} to both sides of the relationship, we
    //are able to save a new user with a new movie entry(the movie is saved too), however we are not able to associate the new user with an existing movie (providing the movie's id field)
    //By Removing the cascade- persist option from both sides, we are able to associate a new user with an existing movie entry(by providing the movie's id), but unable to save a new user and a new movie object
    @ManyToMany()                //creates the junction table
    @JoinTable(name="users_movies",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),inverseJoinColumns=@JoinColumn(name="movie_id",referencedColumnName = "id"))
    private Set<Movie> movies;

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }



    @ManyToMany()
    @JoinTable(name="users_follows",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),inverseJoinColumns=@JoinColumn(name="follows_user_id",referencedColumnName = "id"))
    @JsonIgnore
    private Set<User> following=new HashSet<>();

    @ManyToMany(mappedBy="following")
    private Set<User> followers=new HashSet<>();


    public Set<User> getFollowers() {
        return followers;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void addFollowing(User newUserToFollow){   //add a new user that would like to follow

        this.following.add(newUserToFollow);           //add to the list of teh following users
        newUserToFollow.getFollowers().add(this);  //add to the list of followers of the newUserToFollow user

    }

    public void removeFollowing(User userToUnfollow) {

        this.following.remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(this);

    }


    public void addMovie(Movie movie){
        movies.add(movie);
        movie.getUsers().add(this);
    }


    public void removeMovie(Movie movie){          //the add/remove utility methods are mandatory if you use bidirectional associations so that you can make sure that both sides of the association are in sync.
        movies.remove(movie);
        movie.getUsers().remove(this);
    }

    public User(){

    }

    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
