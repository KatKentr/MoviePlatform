package org.acme.resource;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.TestUtils;
import org.acme.dto.UserDto;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.mapper.UserMapper;
import org.acme.model.User;
import org.acme.service.UserService;
import org.jboss.resteasy.reactive.common.util.RestMediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
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
    @Test
    @TestSecurity(user = "testUser", roles = "ADMIN")
    public void deleteUser_userExists() throws ResourceNotFoundException {

        //deleteUser method is void
        doNothing().when(userService).deleteUserById(user.getId());

        given()
                .when().delete("/"+user.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }


    @Test
    @TestSecurity(user = "testUser", roles = "USER")
    public void deleteUserWithWrongAuthorization() throws ResourceNotFoundException {

        //deleteUser method is void
        doNothing().when(userService).deleteUserById(user.getId());

        given()
                .when().delete("/"+user.getId())
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());

    }


    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN","USER"})
    public void getByIdShouldReturnUser() throws ResourceNotFoundException {

        UserDto userDto=userMapper.toDTO(user);
        when(userService.retrieveUserById(user.getId())).thenReturn(userDto);
        given()
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON)   //json body contains links
                .when().get("/"+userDto.getId())
                .then()
                .log().body()   //print response
                .body("id",equalTo(userDto.getId().intValue()))
                .body("username",equalTo(userDto.getUsername()))
                .body("email",equalTo(userDto.getEmail()))
                .body("role",equalTo(userDto.getRole()))
                .body("password", not(hasKey("password")))
                .body("_links.all-users.href",equalTo("http://localhost:8081/users"))  //check presence of hyperlinks in json response
                .body("_links.self.href",equalTo("http://localhost:8081/users"+ "/" +userDto.getId()));


    }




    }










