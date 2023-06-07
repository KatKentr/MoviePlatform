package org.acme.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.MovieDto;
import org.acme.dto.UserDto;
import org.acme.exceptions.DuplicateResourceException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.mapper.MovieMapper;
import org.acme.mapper.UserMapper;
import org.acme.model.Movie;
import org.acme.model.User;
import org.acme.model.UserMovie;
import org.acme.repository.MovieRepository;
import org.acme.repository.UserMovieRepository;
import org.acme.repository.UserRepository;
import org.acme.service.MovieService;

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
   UserMovieRepository userMovieRepository;


    public UserService(UserRepository userRepository, MovieRepository movieRepository){         //constructor-based injection

        this.userRepository=userRepository;
        this.movieRepository=movieRepository;

    }

    public UserDto saveNewUser(UserDto userDto) throws DuplicateResourceException {

        if (userRepository.existsbyEmail(userDto.getEmail())){

            throw new DuplicateResourceException("User with email "+userDto.getEmail()+" already exists");
        }

        if (userRepository.existsbyUsername(userDto.getUsername())){

            throw new DuplicateResourceException("Username "+userDto.getUsername()+" is already taken");
        }

        User user=userMapper.toEntity(userDto);
        userRepository.persist(user);

        if (userRepository.isPersistent(user)){

            return userMapper.toDTO(user);

        } else {

            throw new NotFoundException();         //TODO: should we check that the object is persisted at this point? or scope of unit test? what a exception should be thrown in this case? i.e when an object is not saved in the db
        }
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

    public void follow(Long userId, User userToFollow) throws ResourceNotFoundException {   //TODO: replace method's argument with Dto data type

        Optional<User> userOptional=userRepository.findByIdOptional(userId);
        User user=userOptional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+userId+" not found"));
        Optional<User> toFollowOptional=userRepository.findByIdOptional(userToFollow.getId());  //else throw userNotFoundException
        User toFollow=toFollowOptional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+userToFollow.getId()+" not found"));
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

    public Set<User> getFollowersOfUser(Long userId) throws ResourceNotFoundException {         //TODO: return dto data type

        Optional<User> userOptional=userRepository.findByIdOptional(userId);
        User user=userOptional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+userId+" not found"));

        return user.getFollowers();

    }

    public Set<User> getFollowingUsers(Long userId) throws ResourceNotFoundException {   //retrieve the users that the user follows

        Optional<User> userOptional=userRepository.findByIdOptional(userId);
        User user=userOptional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+userId+" not found"));

        return user.getFollowing();

    }

    //add a movie for a user
//    public void addMovieToUser(Long userId, MovieDto movieDto) throws ResourceNotFoundException {
//
//        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
//        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
//        Optional<Movie> optionalm=movieRepository.findByTitle(movieDto.getTitle());   //find movie
//        Movie movie=optionalm.orElseThrow(() -> new ResourceNotFoundException("Movie with title: "+movieDto.getTitle()+" does not exist"));
//        user.addMovie(movie);
//
//    }
//
//    public void removeMovieFromUser(Long userId, Long movieId) throws ResourceNotFoundException {
//
//        Optional<User> optional=userRepository.findByIdOptional(userId);
//        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
//        Optional<Movie> optionalm=movieRepository.findByIdOptional(movieId);
//        Movie movie=optionalm.orElseThrow(() -> new ResourceNotFoundException("Movie with id: "+movieId+" does not exist"));
//        if (user.getMovies().contains(movie)){
//
//            user.removeMovie(movie);
//        } else {
//
//            throw new ResourceNotFoundException("User with id "+userId+" has not added to their collection movie with id: "+movieId);
//        }
//
//    }
//
//    public List<MovieDto> getMoviesOfUser(Long userId) throws ResourceNotFoundException {
//
//        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
//        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
//        List<MovieDto> movieDtos=user.getMovies().stream().map(m ->movieMapper.toDto(m)).collect(Collectors.toList());
//        return movieDtos;
//
//    }

//implementation of the add movie to user functionality with the new join table that contains extra columns except for the two primary keys

   // It works!!

//    public void addMovieToUser(Long userId, MovieDto movieDto) throws ResourceNotFoundException {
//
//        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
//        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
//        Optional<Movie> optionalm=movieRepository.findByTitle(movieDto.getTitle());   //find movie
//        Movie movie=optionalm.orElseThrow(() -> new ResourceNotFoundException("Movie with title: "+movieDto.getTitle()+" does not exist"));
//
//        UserMovie.UserMovieId userMovieId=new UserMovie.UserMovieId(user.getId(), movie.getId());
//        UserMovie userMovie=new UserMovie();
//        userMovie.setPrimaryKey(userMovieId);
//        userMovie.setMovie(movie);
//        userMovie.setUser(user);
//
//        userMovieRepository.persist(userMovie);        //save the user-movie association in the users_movies table
//
//    }
//
//    public void removeMovieFromUser(Long userId, Long movieId) throws ResourceNotFoundException { //TODO:See the method implemented in UserMovieRepository
//
//        UserMovie.UserMovieId userMovieId=new UserMovie.UserMovieId(userId, movieId);
//        //Optional<UserMovie> optionalm=userMovieRepository.findByCompositePrimaryKey(userId,movieId);   //find movie        how can we remove it??
//        Optional<UserMovie> optionalm=userMovieRepository.findByCompositePrimaryKey(userMovieId);
//        UserMovie userMovie=optionalm.orElseThrow(() -> new ResourceNotFoundException("exception"));
//        userMovieRepository.delete(userMovie);   //remove entry from users_movies table
//
//    }
//
//        public List<MovieDto> getMoviesOfUser(Long userId) throws ResourceNotFoundException {
//
//        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
//        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
//        List<UserMovie> moviesOfUser=userMovieRepository.findByUser(user);
//        List<MovieDto> movieDtos=moviesOfUser.stream().map( x-> movieMapper.toDto(x.getMovie())).collect(Collectors.toList());
//        return movieDtos;
//
//    }


    //implementation inspired from https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/
    //Note: the following methods now work as expected. We can add an existing movie to the user, remove the movie form the user, and retrieve movies for the user
    //TODO:If we delete a user all the entries in the  users_movies table should be deleted, but not the movies! . It works!!!
    //TODO: if we delete a movie all the entries in the users_movies table associated with this movie should be deleted, but not the users! It works!

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

    //TODO: add review and rate to a movie

    public void addRateToMovie(Long userId, Long movieId,int rate) throws ResourceNotFoundException {
        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
        //should we check that the mvovie exists in the db? Or directly if a movie with this id is related to a user?
        Optional<UserMovie> optUserMovie=user.getMovies().stream().filter( x -> x.getMovie().getId()==movieId).findFirst();
        UserMovie userMovie=optUserMovie.orElseThrow(() -> new ResourceNotFoundException("User has not added movie with id: "+movieId+" in their collection"));
        userMovie.setRate(rate);
        //TODO: how we could display the movie with this information?

    }











    public void deleteUserById(Long userId) throws ResourceNotFoundException {

        Optional<User> optional=userRepository.findByIdOptional(userId); //find user

        if (optional.isEmpty()){

            throw new ResourceNotFoundException("User with id: "+userId+" does not exist");
        }
        userRepository.deleteById(userId);

    }


    //TODO:retrieve by name




}
