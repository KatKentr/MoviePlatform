package org.acme.resource;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.acme.TestUtils;
import org.acme.dto.UserDto;
import org.acme.mapper.UserMapper;
import org.acme.model.User;
import org.acme.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
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
    public void usersShouldReturnAllUsers(){

         List<UserDto> userDtos= List.of(userMapper.toDTO(user),userMapper.toDTO(userAdmin));

         when(userService.retrieveAllUsers()).thenReturn(userDtos);

        given()
                .when().get()
                .then()
                .statusCode(200);



    }






}
