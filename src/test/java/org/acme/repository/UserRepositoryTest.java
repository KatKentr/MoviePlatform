package org.acme.repository;


import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.model.User;
import org.acme.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class UserRepositoryTest {

  @Inject
  private UserRepository userRepository;

  private User validUser;


   @BeforeEach
    public void setUp(){

       validUser =new User();                      //initialize a valid user
       validUser.setUsername("Testperson");
       validUser.setEmail("test@example.com");
       validUser.setPassword("1234");
       validUser.setRole("USER");

  }



  @Test
  @TestTransaction //The test method will be run in a transaction, but roll back once the test method is complete and revert any db changes
  public void validUserShouldbeSaved(){

       userRepository.persist(validUser);
       User fetchedUser=userRepository.findById(validUser.getId());
       //assert not null and check name.
      Assertions.assertNotNull(fetchedUser);
      Assertions.assertNotNull(fetchedUser.getId());
      assertEquals(validUser.getUsername(),fetchedUser.getUsername());
      assertEquals(validUser.getEmail(),fetchedUser.getEmail());
      //System.out.println(fetchedUser.getId()+" "+fetchedUser.getUsername());
  }



  @Test
  @TestTransaction
  public void listAll_shouldBeAbleToReturnAllUsersInAList(){

      User validUser2 =new User();                      //initialize a second valid user
      validUser2.setUsername("Testperson2");
      validUser2.setEmail("test2@example.com");
      validUser2.setPassword("1234");
      validUser2.setRole("ADMIN");

      userRepository.persist(validUser);
      userRepository.persist(validUser2);

      List<User> users=userRepository.listAll();

      assertEquals(users.size(),2);
      assertEquals(users.get(1).getUsername(),validUser2.getUsername());


  }
    @Test
    @TestTransaction
    public void findUserbyIdTest_exists() {

       userRepository.persist(validUser);
       System.out.println("validUser has id: "+validUser.getId());
       Optional<User> opt=userRepository.findByIdOptional(validUser.getId());
       Assertions.assertTrue(opt.isPresent());
       assertEquals(opt.get().getId(),validUser.getId());
       assertEquals(opt.get().getUsername(),validUser.getUsername());

    }

    //findbyUsername exists
    @Test
    @TestTransaction
    public void findbyUsername_exists() {

        userRepository.persist(validUser);
        Optional<User> opt=userRepository.findByUsername(validUser.getUsername());
        Assertions.assertTrue(opt.isPresent());
        assertEquals(opt.get().getId(),validUser.getId());
        assertEquals(opt.get().getUsername(),validUser.getUsername());

    }


    @Test
    @TestTransaction
    public void findbyUsername_notExists() {

        Optional<User> opt=userRepository.findByUsername(validUser.getUsername());
        Assertions.assertTrue(opt.isEmpty());

    }

    //existsby Username and exists by Email

    @Test
    @TestTransaction
    public void existsByUsername_exists() {

        userRepository.persist(validUser);
        Boolean bool=userRepository.existsbyUsername(validUser.getUsername());
        Assertions.assertTrue(bool);

    }

    @Test
    @TestTransaction
    public void existsByUsername_notExists() {

        Boolean bool=userRepository.existsbyUsername(validUser.getUsername());
        assertEquals(bool,false);

    }


    @Test
    @TestTransaction
    public void existsByEmail_exists() {

        userRepository.persist(validUser);
        Boolean bool=userRepository.existsbyEmail(validUser.getEmail());
        Assertions.assertTrue(bool);

    }

    @Test
    @TestTransaction
    public void existsByEmail_notExists() {

        Boolean bool=userRepository.existsbyEmail(validUser.getEmail());
        assertEquals(bool,false);

    }

















}
