package org.acme;

import org.acme.model.User;

public class TestUtils {


    private User user;


    public TestUtils(){}


    public User createValidUserwithRoleUser(){

        user =new User();                      //initialize a valid user
        user.setUsername("Testperson");
        user.setEmail("test@example.com");
        user.setPassword("1234");
        user.setRole("USER");
        user.setId(1L);

        return user;

    }


    public User createValidUserwithRoleAdmin(){

        user =new User();                      //initialize a valid user
        user.setUsername("Testperson2");
        user.setEmail("test2@example.com");
        user.setPassword("1234");
        user.setRole("ADMIN");
        user.setId(1L);

        return user;

    }





}
