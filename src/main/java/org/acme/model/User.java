package org.acme.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.*;


//A user can select their favorite movies
@Entity
@Table(name="users")
@UserDefinition
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Username
    @NotNull                              //TODO: maybe apply some more restrictions on these fields
    private String username;

    @NotNull
    private String email;

    @Password
    @NotNull
    private String password;


    @Roles
    @NotNull
    public String role;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {          //we don't want to store plain raw passwords in the db, so we call the hash function in the setPassowrd method

        this.password = BcryptUtil.bcryptHash(password);
    }


    //Inspired from: https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,   //A short investigation on that
            orphanRemoval = true
    )
    @JsonIgnore
    private List<UserMovie> movies = new ArrayList<>();

    public void addMovie(Movie movie){               //addMovie and removeMovie utility methods are required by every biderectional association, to make sure that both sides of the association are in sync.

        UserMovie userMovie=new UserMovie(this,movie);     //create a new instance of UserMovie
        movies.add(userMovie);                                  //add the instance to the movies list
        movie.getUsers().add(userMovie);
        //movies.stream().map(m->m.getMovie().getTitle()).forEach(System.out::println);  debuging purposes
    }

    public void removeMovie(Movie movie){
                                             //we need to remove this user-movie association
        for (Iterator<UserMovie> iterator = movies.iterator(); iterator.hasNext();){

            UserMovie userMovie=iterator.next();

            if (userMovie.getUser().equals(this) && userMovie.getMovie().equals(movie)){

                iterator.remove();     //remove the userMovie instance from the movies list
                userMovie.getMovie().getUsers().remove(userMovie);  //remove this userMovie instance from the users list of the Movie entity
                userMovie.setUser(null);   //todo:what about the other properties of the userMovie instance? rate, review, addetAt
                userMovie.setMovie(null);
            }

        }

    }

    //we check if two User instances are equal based on Id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
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

    public List<UserMovie> getMovies() {
        return movies;
    }

    public void setMovies(List<UserMovie> movies) {
        this.movies = movies;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
