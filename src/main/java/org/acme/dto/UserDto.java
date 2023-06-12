package org.acme.dto;

import jakarta.validation.constraints.NotNull;
import org.acme.model.Movie;

import java.util.List;
import java.util.Set;

public class UserDto {


    private Long id;


    @NotNull
    private String username;

    @NotNull
    private String email;


    @NotNull
    private String role;

//    @NotNull
//    private String password;
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

//    private List<Movie> movies;   //TODO: Do we really need to load the movies of each user? We could remove this

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

//    public List<Movie> getMovies() {
//        return movies;
//    }
//
//    public void setMovies(List<Movie> movies) {
//        this.movies = movies;
//    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
