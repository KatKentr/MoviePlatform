package org.acme.dto;

import jakarta.persistence.*;
import org.acme.model.Movie;
import org.acme.model.User;
import org.acme.model.UserMovie;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

public class UserMovieDto {

    //private UserMovie.UserMovieId primaryKey;    we dont want to expose composite key and all user fields


    //private User user;

    //user details
    private String username;
    private String email;


    private Movie movie;

    private int rate;

    private String review;

    private Date addedAt;


    public UserMovieDto(){

    }

   public UserMovieDto(Movie movie, int rate, String review, Date addedAt, String username, String email) {
        this.movie = movie;
        this.rate = rate;
        this.review = review;
        this.addedAt = addedAt;
        this.username=username;
        this.email=email;
    }


    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
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
