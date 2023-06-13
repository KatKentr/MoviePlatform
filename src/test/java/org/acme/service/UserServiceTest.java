package org.acme.service;

import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.TestUtils;
import org.acme.dto.UserDto;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.model.User;
import org.acme.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@QuarkusTest
public class UserServiceTest {


    @Inject
    UserService userService;


    @InjectMock
    UserRepository userRepository;


    private User validUser, validAdmin;

    private TestUtils testUtils=new TestUtils();   //instantiate testUtils


    @BeforeEach
    public void setUp(){

        //initialize a valid user

        validUser =testUtils.createValidUserwithRoleUser();

    }


    @Test
    public void getUserById_userExists() throws ResourceNotFoundException {
        when(userRepository.findByIdOptional(validUser.getId())).thenReturn(Optional.ofNullable(validUser));
        UserDto userDto=userService.retrieveUserById(validUser.getId());
        assertEquals(validUser.getUsername(),userDto.getUsername());
        assertEquals(validUser.getEmail(),userDto.getEmail());
        assertEquals(validUser.getRole(),userDto.getRole());

    }


    @Test
    public void getUserById_NotExists() throws ResourceNotFoundException {
        when(userRepository.findByIdOptional(validUser.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.retrieveUserById(validUser.getId());
        });
        Assertions.assertTrue(exception.getMessage().contains("user with id: "+validUser.getId()+ " not found"));
    }




}
