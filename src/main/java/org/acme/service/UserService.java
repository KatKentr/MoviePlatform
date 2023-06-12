package org.acme.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.MovieDto;
import org.acme.dto.UserDto;
import org.acme.dto.UserMovieDto;
import org.acme.exceptions.DuplicateResourceException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.mapper.MovieMapper;
import org.acme.mapper.UserMapper;
import org.acme.mapper.UserMovieMapper;
import org.acme.model.Movie;
import org.acme.model.User;
import org.acme.model.UserMovie;
import org.acme.repository.MovieRepository;
import org.acme.repository.UserMovieRepository;
import org.acme.repository.UserRepository;
import org.acme.service.MovieService;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped                 //an application-wide singleton that is “injectable”.
public class UserService {

    UserRepository userRepository;

    MovieRepository movieRepository;

   @Inject
    UserMapper userMapper;

   @Inject
    MovieMapper movieMapper;


   @Inject
    UserMovieMapper userMovieMapper;


    public UserService(UserRepository userRepository, MovieRepository movieRepository){         //constructor-based injection

        this.userRepository=userRepository;
        this.movieRepository=movieRepository;

    }


    public List<UserDto> retrieveAllUsers(){

        List<User> users=userRepository.listAll();
        List<UserDto> usersDto=users.stream().map(u->userMapper.toDTO(u)).collect(Collectors.toList());
        //System.out.println(usersDto.get(0).getMovies());
        return usersDto;


    }

    public UserDto retrieveUserById(Long id) throws ResourceNotFoundException {
        Optional<User> optional=userRepository.findByIdOptional(id);
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+id+" not found"));
        return userMapper.toDTO(user);

    }

    public void follow(Long userId, String usernameToFollow) throws ResourceNotFoundException {

        Optional<User> userOptional=userRepository.findByIdOptional(userId);
        User user=userOptional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+userId+" not found"));
        Optional<User> toFollowOptional=userRepository.findByUsername(usernameToFollow);  //else throw userNotFoundException
        User toFollow=toFollowOptional.orElseThrow(() -> new ResourceNotFoundException("user with username: "+usernameToFollow+" not found"));
        user.addFollowing(toFollow);

    }


    public void unfollow(Long userId, Long toUnfollowId) throws ResourceNotFoundException {
        Optional<User> userOptional=userRepository.findByIdOptional(userId);
        User user=userOptional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+userId+" not found"));
        Optional<User> toUnfollowOptional=userRepository.findByIdOptional(toUnfollowId);
        User toUnfollow=toUnfollowOptional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+toUnfollowId+" not found"));
        user.removeFollowing(toUnfollow); //TODO: should we check first that the user indeed follows the second user?

    }

    //TO CHECK: Does Hibernate executes the sql queries in the backroound?

    public Set<UserDto> getFollowersOfUser(Long userId) throws ResourceNotFoundException {

        Optional<User> userOptional=userRepository.findByIdOptional(userId);
        User user=userOptional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+userId+" not found"));

        return user.getFollowers().stream().map( u-> userMapper.toDTO(u)).collect(Collectors.toSet());

    }

    public Set<UserDto> getFollowingUsers(Long userId) throws ResourceNotFoundException {   //retrieve the users that the user follows

        Optional<User> userOptional=userRepository.findByIdOptional(userId);
        User user=userOptional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+userId+" not found"));

        return user.getFollowing().stream().map(u->userMapper.toDTO(u)).collect(Collectors.toSet());

    }


    //Note: the following methods now work as expected. We can add an existing movie to the user, remove the movie form the user, and retrieve movies for the user

    public void addMovieToUser(Long userId, MovieDto movieDto) throws ResourceNotFoundException {

        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
        Optional<Movie> optionalm=movieRepository.findByTitle(movieDto.getTitle());   //find movie
        Movie movie=optionalm.orElseThrow(() -> new ResourceNotFoundException("Movie with title: "+movieDto.getTitle()+" does not exist"));

        user.addMovie(movie);
    }


        public void removeMovieFromUser(Long userId, Long movieId) throws ResourceNotFoundException { //TODO:See the method implemented in UserMovieRepository

        Optional<User> optional=userRepository.findByIdOptional(userId);
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
        Optional<Movie> optionalm=movieRepository.findByIdOptional(movieId);
        Movie movie=optionalm.orElseThrow(() -> new ResourceNotFoundException("Movie with id: "+movieId+" does not exist"));
        user.removeMovie(movie);

    }

    public List<MovieDto> getMoviesOfUser(Long userId) throws ResourceNotFoundException {

        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
        //List<UserMovie> moviesOfUser=userMovieRepository.findByUser(user);
        List<MovieDto> movieDtos=user.getMovies().stream().map( x-> movieMapper.toDto(x.getMovie())).collect(Collectors.toList());
        return movieDtos;

    }



    public UserMovieDto addRateToMovie(Long userId, Long movieId,@PositiveOrZero(message="number should be a postive integer") @Max(value=10, message="Please rate from 0-10") int rate) throws ResourceNotFoundException {  //TODO: add validation constraint to check that the rate provided represents a number
        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
        //should we check that the mvovie exists in the db? Or directly if a movie with this id is related to a user?
        Optional<UserMovie> optUserMovie=user.getMovies().stream().filter( x -> x.getMovie().getId()==movieId).findFirst();
        UserMovie userMovie=optUserMovie.orElseThrow(() -> new ResourceNotFoundException("User has not added movie with id: "+movieId+" in their collection"));
        userMovie.setRate(rate);
        //Hibernate.initialize(userMovie.getUser());
        Hibernate.initialize(userMovie.getMovie());
        return userMovieMapper.toDto(userMovie,user);

    }

    public UserMovieDto addReviewToMovie(Long userId, Long movieId, @Size(min = 2,message = "review should be at least two characters") @NotNull String review) throws ResourceNotFoundException {
        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
        //should we check that the mvovie exists in the db? Or directly if a movie with this id is related to a user?
        Optional<UserMovie> optUserMovie=user.getMovies().stream().filter( x -> x.getMovie().getId()==movieId).findFirst();
        UserMovie userMovie=optUserMovie.orElseThrow(() -> new ResourceNotFoundException("User has not added movie with id: "+movieId+" in their collection"));
        userMovie.setReview(review);
        //Hibernate.initialize(userMovie.getUser());
        Hibernate.initialize(userMovie.getMovie());
        return userMovieMapper.toDto(userMovie,user);

    }



    //Deleting a user, automatically deletes their relationship in the users_movies table as well as the users_follows table

    public void deleteUserById(Long userId) throws ResourceNotFoundException {

        Optional<User> optional=userRepository.findByIdOptional(userId); //find user

        if (optional.isEmpty()){

            throw new ResourceNotFoundException("User with id: "+userId+" does not exist");
        }
        userRepository.deleteById(userId);

    }


    //TODO:retrieve by name




}
