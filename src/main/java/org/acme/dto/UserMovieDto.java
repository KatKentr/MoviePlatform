package org.acme.dto;

import jakarta.persistence.*;
import org.acme.model.Movie;
import org.acme.model.User;
import org.acme.model.UserMovie;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

public class UserMovieDto {

    private UserMovie.UserMovieId primaryKey;


    //private User user;


    private Movie movie;

    private int rate;

    private String review;

    private Date addedAt;


    public UserMovieDto(){

    }

   public UserMovieDto(UserMovie.UserMovieId primaryKey, Movie movie, int rate, String review, Date addedAt) {
        this.primaryKey = primaryKey;
        //this.user = user;
        this.movie = movie;
        this.rate = rate;
        this.review = review;
        this.addedAt = addedAt;
    }

    public UserMovie.UserMovieId getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(UserMovie.UserMovieId primaryKey) {
        this.primaryKey = primaryKey;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

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
}
