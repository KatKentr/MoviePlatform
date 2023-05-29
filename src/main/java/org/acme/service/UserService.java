package org.acme.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.UserDto;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.model.User;
import org.acme.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository){         //constructor-based injection

        this.userRepository=userRepository;

    }

    public UserDto saveNewUser(UserDto userDto){

        User user=mapToEntity(userDto);
        userRepository.persist(user);

        if (userRepository.isPersistent(user)){

            return mapToDto(user);

        } else {

            throw new NotFoundException();         //TODO: what a exception should be thrown in this case? i.e when an object is not saved in the db
        }
    }


    public List<UserDto> retrieveAllUsers(){

        List<User> users=userRepository.listAll();
        List<UserDto> usersDto=users.stream().map(u->mapToDto(u)).collect(Collectors.toList());
        return usersDto;


    }

    public UserDto retrieveUserById(Long id) throws ResourceNotFoundException {
        Optional<User> optional=userRepository.findByIdOptional(id);
        User user=optional.orElseThrow(() -> new ResourceNotFoundException("user with id: "+id+" not found")); //TODO:replace with user not found exception
        return mapToDto(user);

    }

    public void follow(Long userId, User userToFollow){

        User user=userRepository.findById(userId);
        User toFollow=userRepository.findById(userToFollow.getId());  //else throw userNotFoundException
        user.addFollowing(toFollow);

    }


    public void unfollow(Long userId, Long toUnfollowId){
        User user=userRepository.findById(userId);
        User toFollow=userRepository.findById(toUnfollowId);  //todo:else throw userNotFoundException
        user.removeFollowing(toFollow);

    }

    //TO CHECK: Does Hibernate executes the sql queries in the backroound?

    public Set<User> getFollowers(Long userId){

        return userRepository.findById(userId).getFollowers();

    }

    public Set<User> getFollowing(Long userId){

        return userRepository.findById(userId).getFollowing();

    }







    //TODO:retrieve by name

    //TODO: add favorite movies to user
    //TODO:remove favorite movies from user


    private UserDto mapToDto(User user){
       UserDto userDto=new UserDto();
       userDto.setId(user.getId());
       userDto.setUsername(user.getUsername());
       userDto.setEmail(user.getEmail());
       userDto.setPassword(user.getPassword());
       //Include also movies?
       return userDto;
    }


    private User mapToEntity(UserDto userDto){

      User user=new User();
      user.setUsername(userDto.getUsername());
      user.setEmail(userDto.getEmail());
      user.setPassword(userDto.getPassword());
      return user;

    }




}
