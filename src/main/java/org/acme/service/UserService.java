package org.acme.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.MovieDto;
import org.acme.dto.UserDto;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.mapper.MovieMapper;
import org.acme.mapper.UserMapper;
import org.acme.model.Movie;
import org.acme.model.User;
import org.acme.repository.MovieRepository;
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


    public UserService(UserRepository userRepository, MovieRepository movieRepository){         //constructor-based injection

        this.userRepository=userRepository;
        this.movieRepository=movieRepository;

    }

    public UserDto saveNewUser(UserDto userDto){

        User user=userMapper.toEntity(userDto);
        userRepository.persist(user);

        if (userRepository.isPersistent(user)){

            return userMapper.toDTO(user);

        } else {

            throw new NotFoundException();         //TODO: what a exception should be thrown in this case? i.e when an object is not saved in the db
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

    public Set<User> getFollowers(Long userId){         //TODO: return dto data type

        return userRepository.findById(userId).getFollowers();

    }

    public Set<User> getFollowing(Long userId){

        return userRepository.findById(userId).getFollowing();

    }

    //add a movie for a user
    public void addMovieToUser(Long userId, MovieDto movieDto) throws ResourceNotFoundException {

        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
        Optional<Movie> optionalm=movieRepository.findByTitle(movieDto.getTitle());   //find movie
        Movie movie=optionalm.orElseThrow(() -> new ResourceNotFoundException("Movie with title: "+movieDto.getTitle()+" does not exist"));
        user.addMovie(movie);

    }

    public void removeMovieFromUser(Long userId, Long movieId) throws ResourceNotFoundException {

        Optional<User> optional=userRepository.findByIdOptional(userId);
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
        Optional<Movie> optionalm=movieRepository.findByIdOptional(movieId);
        Movie movie=optionalm.orElseThrow(() -> new ResourceNotFoundException("Movie with id: "+movieId+" does not exist"));
        if (user.getMovies().contains(movie)){

            user.removeMovie(movie);
        } else {

            throw new ResourceNotFoundException("User with id "+userId+" has not added to their collection movie with id: "+movieId);
        }

    }

    public List<MovieDto> getMoviesOfUser(Long userId) throws ResourceNotFoundException {

        Optional<User> optional=userRepository.findByIdOptional(userId); //find user
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("User with id: "+userId+"does not exist"));
        List<MovieDto> movieDtos=user.getMovies().stream().map(m ->movieMapper.toDto(m)).collect(Collectors.toList());
        return movieDtos;

    }



    //TODO:retrieve by name




}
