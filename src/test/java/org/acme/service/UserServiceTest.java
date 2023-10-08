package org.acme.service;

import io.quarkus.test.Mock;
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
    public void followAUser_succesfull() throws ResourceNotFoundException {               //OPEN QUESTION: the method follow(Long id, String name) of the Service class that we want to test, is a void method. Therefore, we could use verify() to check the invocation of the void method user.addFollowing(), which is invoked inside the method follow: approach A. However, we don't check with this approach if the followings set includes the user added. Another approach would be not to work with a mock user, instead we invoke the follow method and check the size of the list by getFollowings(): Approach B

        //APPROACH A

//        User userMock=testUtils.createValidUserMockwithRoleUser();
//        validAdmin=testUtils.createValidUserwithRoleAdmin();
//        String usernameToFollow=validAdmin.getUsername();
//        when(userRepository.findByIdOptional(userMock.getId())).thenReturn(Optional.ofNullable(userMock));
//        when(userRepository.findByUsername(usernameToFollow)).thenReturn(Optional.ofNullable(validAdmin));
//        userService.follow(userMock.getId(),usernameToFollow);
//        verify(userMock,times(1)).addFollowing(validAdmin);         //we check that the method is invoked once
////        assertEquals(1,userMock.getFollowing().size());    does not work  //TO INVESTIGATE: How do we check then that the user is added in the addFollowing List?

        //APPROACH B

        User userToFollow=testUtils.createValidUserwithRoleAdmin();
        String usernameToFollow=userToFollow.getUsername();
        when(userRepository.findByIdOptional(user.getId())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByUsername(usernameToFollow)).thenReturn(Optional.ofNullable(userToFollow));
        userService.follow(user.getId(),usernameToFollow);
        assertEquals(1,user.getFollowing().size());     //We check the size of the followings set
        assertTrue(user.getFollowing().contains(userToFollow));
        //user.getFollowing().stream().forEach( x -> System.out.println(x.getUsername()));

    }


     //unfollow method: similar with follow 3 test cases
     @Test
     public void unfollow_UsertoUnfollowNotFound(){

         Long userIdToUnfollow=2L;
         when(userRepository.findByIdOptional(user.getId())).thenReturn(Optional.ofNullable(user));
         when(userRepository.findByIdOptional(userIdToUnfollow)).thenReturn(Optional.empty());
         Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
             userService.unfollow(user.getId(),userIdToUnfollow);
         });
         Assertions.assertTrue(exception.getMessage().contains("user with id: "+userIdToUnfollow+" not found"));
     }

    @Test
    public void unfollow_UserNotFound(){

        User userToUnfollow=new User(2L,"random","random@example.com");
        when(userRepository.findByIdOptional(user.getId())).thenReturn(Optional.empty());
        when(userRepository.findByIdOptional(userToUnfollow.getId())).thenReturn(Optional.ofNullable(userToUnfollow));
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.unfollow(user.getId(),userToUnfollow.getId());
        });
        Assertions.assertTrue(exception.getMessage().contains("user with id: "+user.getId()+" not found"));
    }


    //getFollowersOfUser

    @Test
    public void getFollowersOfUserShouldReturnFollowers() throws ResourceNotFoundException {


        User userMock=testUtils.createValidUserMockwithRoleUser();

        when(userRepository.findByIdOptional(userMock.getId())).thenReturn(Optional.ofNullable(userMock));
        User follower=testUtils.createValidUserwithRoleAdmin();
//        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        //userService.follow(follower.getId(),user.getUsername());         //follower follows user  //TO INVESTIGATE: It seems that we are invoking (testing?) two methods of the useService, not sure if ti is the correct approach.
        when(userMock.getFollowers()).thenReturn(Set.of(follower));
        Set<UserDto> userDtos=userService.getFollowersOfUser(userMock.getId());  //the method we want to test
        assertEquals(1,userDtos.size());
       // assertTrue(userDtos.contains(follower));  Note: does not work because we have not overriden equals in UserDto
        //userDtos.stream().forEach( x -> System.out.println(x.getUsername()));

    }


    //++ test other methods


















}
