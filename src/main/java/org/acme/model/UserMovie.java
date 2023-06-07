package org.acme.model;


//mapping the join table: users_movies

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


//Since we want to add more columns than the FK user_id and movie_id in the join table, we need to map the join table using a dedicated entity
@Entity
@Table(name="users_movies")
public class UserMovie {

    //@EmbeddedId: used for embedding a composite-id class as the primary key of this mapping.
    //composite-id key
    @EmbeddedId
    //private UserMovieId primaryKey=new UserMovieId();
    private UserMovieId primaryKey;

    @ManyToOne(fetch = FetchType.LAZY)   //by default is EAGER
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movie_id")
    private Movie movie;

    private int rate;

    //@NotEmpty(message="review text should not be empty")
    private String review;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false)
    private Date addedAt;

    public UserMovie(){}            //could also be private?

    public UserMovie(User user, Movie movie) {   //constructor
        this.user=user;
        this.movie=movie;
        this.primaryKey=new UserMovieId(user.getId(),movie.getId());
    }

    public UserMovieId getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(UserMovieId primaryKey) {
        this.primaryKey = primaryKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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


    //we create a composite-ID class for the composite key
    //@Embeddable annotation: used so that the class can be embedded in other entities
    @Embeddable
    public static class UserMovieId implements Serializable{

        private Long user_id;
        private Long movie_id;


        private UserMovieId(){}   //should be public?

        public UserMovieId(Long user_id, Long movie_id) {
            super();
            this.user_id= user_id;
            this.movie_id = movie_id;
        }

        public Long getUser_id() {
            return user_id;
        }

        public Long getMovie_id() {
            return movie_id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserMovieId)) return false;
            UserMovieId that = (UserMovieId) o;
            return Objects.equals(user_id, that.user_id) && Objects.equals(movie_id, that.movie_id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user_id, movie_id);
        }
    }







}
