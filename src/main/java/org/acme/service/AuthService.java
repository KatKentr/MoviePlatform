package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.SignupDto;
import org.acme.dto.UserDto;
import org.acme.exceptions.DuplicateResourceException;
import org.acme.mapper.UserMapper;
import org.acme.model.User;
import org.acme.repository.UserRepository;


@ApplicationScoped
public class AuthService {


    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;


    public UserDto saveNewUser(SignupDto signupDto) throws DuplicateResourceException {  //new user registers

        if (userRepository.existsbyEmail(signupDto.getEmail())){

            throw new DuplicateResourceException("User with email "+signupDto.getEmail()+" already exists");
        }

        if (userRepository.existsbyUsername(signupDto.getUsername())){

            throw new DuplicateResourceException("Username "+signupDto.getUsername()+" is already taken");
        }

        User user=userMapper.fromSignUpToUser(signupDto);
        userRepository.persist(user);

        if (userRepository.isPersistent(user)){

            return userMapper.toDTO(user);

        } else {

            throw new NotFoundException();         //TODO: should we check that the object is persisted at this point? or scope of unit test? what a exception should be thrown in this case? i.e when an object is not saved in the db
        }
    }



}
