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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
public class UserServiceTest {


    @Inject
    UserService userService;


    @InjectMock
    UserRepository userRepository;


    private User user, validAdmin;

    private TestUtils testUtils=new TestUtils();   //instantiate testUtils


    @BeforeEach
    public void setUp(){

        //initialize a valid user

        user =testUtils.createValidUserwithRoleUser();

    }


    @Test
    public void getUserById_userExists() throws ResourceNotFoundException {
        when(userRepository.findByIdOptional(user.getId())).thenReturn(Optional.ofNullable(user));
        UserDto userDto=userService.retrieveUserById(user.getId());
        assertEquals(user.getUsername(),userDto.getUsername());
        assertEquals(user.getEmail(),userDto.getEmail());
        assertEquals(user.getRole(),userDto.getRole());

    }


    @Test
    public void getUserById_NotExists() throws ResourceNotFoundException {
        when(userRepository.findByIdOptional(user.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.retrieveUserById(user.getId());
        });
        Assertions.assertTrue(exception.getMessage().contains("user with id: "+ user.getId()+ " not found"));
    }


    @Test
    public void retrieveAllUsersShouldReturnAllUsers(){

        validAdmin=testUtils.createValidUserwithRoleAdmin();
        when(userRepository.listAll()).thenReturn(List.of(validAdmin, user));
        List<UserDto> userDtos=userService.retrieveAllUsers();  //the method returns dtos
        assertEquals(2,userDtos.size());


    }


    //tests for method follow, 3 test cases, 1 valid and 1 to test the exception thrown


    @Test
    public void followAUser_UsertoFollowNotFound(){

        String usernameToFollow="Foufoutos";
        when(userRepository.findByIdOptional(user.getId())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByUsername(usernameToFollow)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.follow(user.getId(),usernameToFollow);
        });
        Assertions.assertTrue(exception.getMessage().contains("user with username: "+usernameToFollow+" not found"));
    }

    @Test
    public void followAUser_UserNotFound(){

        String usernameToFollow="Foufoutos";
        when(userRepository.findByIdOptional(user.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.follow(user.getId(),usernameToFollow);
        });
        Assertions.assertTrue(exception.getMessage().contains("user with id: "+ user.getId()+" not found"));
    }



    // Testing of a void method: with verify (check also ArgumentCaptor)
    @Test
    public void followAUser_succesfull() throws ResourceNotFoundException {

        User user = mock(User.class);
        user.setUsername("Testperson");
        user.setEmail("test@example.com");
        user.setPassword("1234");
        user.setRole("USER");
        user.setId(1L);


        validAdmin=testUtils.createValidUserwithRoleAdmin();
        String usernameToFollow=validAdmin.getUsername();
        when(userRepository.findByIdOptional(user.getId())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByUsername(usernameToFollow)).thenReturn(Optional.ofNullable(validAdmin));
        userService.follow(user.getId(),usernameToFollow);
        verify(user,times(1)).addFollowing(validAdmin);         //we check that the method is invoked once

    }


     //unfollow method: similar with follow 3 test cases


    //getFollowersOfUser

    @Test
    public void getFollowersOfUserShouldReturnFollowers() throws ResourceNotFoundException {


        User user = mock(User.class);
        user.setUsername("Testperson");
        user.setEmail("test@example.com");
        user.setPassword("1234");
        user.setRole("USER");
        user.setId(1L);

        when(userRepository.findByIdOptional(user.getId())).thenReturn(Optional.ofNullable(user));
        User follower=testUtils.createValidUserwithRoleAdmin();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        //userService.follow(follower.getId(),user.getUsername());         //follower follows user  //TO INVESTIGATE: It seems that we are invoking (testing?) two methods of the useService, not sure if ti is the correct approach.
        when(user.getFollowers()).thenReturn(Set.of(follower));
        Set<UserDto> userDtos=userService.getFollowersOfUser(user.getId());
        assertTrue(!userDtos.isEmpty());
        assertEquals(1,userDtos.size());

    }


















}
