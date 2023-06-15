package org.acme.resource;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.TestUtils;
import org.acme.dto.UserDto;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.mapper.UserMapper;
import org.acme.model.User;
import org.acme.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;


@QuarkusTest
@TestHTTPEndpoint(UserResource.class)
public class UserResourceTest {

    @InjectMock
    UserService userService;


    @Inject
    UserMapper userMapper;


    private User user, userAdmin;


    private TestUtils testUtils=new TestUtils();


    @BeforeEach
    public void setup(){

        user=testUtils.createValidUserwithRoleUser();
        userAdmin=testUtils.createValidUserwithRoleAdmin();


    }


    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN","USER"})
    public void usersShouldReturnAllUsers(){

         List<UserDto> userDtos= List.of(userMapper.toDTO(user),userMapper.toDTO(userAdmin));

         when(userService.retrieveAllUsers()).thenReturn(userDtos);

        given()
                .when().get()
                .then()
                //.log().body()   //print response
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()",equalTo(userDtos.size()))
                .body("username",hasItems(user.getUsername(),userAdmin.getUsername()))
                .body("password", not(hasKey("password")));   //check that password is not part of the json response

    }


    @Test
    public void userswithoutAuthorizationShouldReturnUnauthorized(){

        List<UserDto> userDtos= List.of(userMapper.toDTO(user),userMapper.toDTO(userAdmin));

        when(userService.retrieveAllUsers()).thenReturn(userDtos);

        given()
                .when().get()
                .then()
                //.log().body()   //print response
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());

    }

    //TODO
//    @Test
//    @TestSecurity(user = "testUser", roles = "USER")
//    public void deleteUserWithWrongAuthorization(){
//
//
//    }


    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN","USER"})
    public void getByIdShouldReturnUser() throws ResourceNotFoundException {

        UserDto userDto=userMapper.toDTO(user);
        when(userService.retrieveUserById(user.getId())).thenReturn(userDto);
        given()
                .when().get("/"+userDto.getId())
                .then()
                .log().body()   //print response
                .body("id",equalTo(userDto.getId().intValue()))
                .body("username",equalTo(userDto.getUsername()))
                .body("email",equalTo(userDto.getEmail()))
                .body("role",equalTo(userDto.getRole()))
                .body("password", not(hasKey("password")))
                .statusCode(Response.Status.OK.getStatusCode());

    }




    }










