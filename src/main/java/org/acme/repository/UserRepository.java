package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.rest.data.panache.ResourceProperties;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.UserDto;
import org.acme.model.Movie;
import org.acme.model.User;

import java.util.List;

//Thought: support pagination? The process of diving a large dataset into smaller chunks(pages)
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findByName(String firstName){  //now we can make a case-insensitive search for movies belonging to the same director

        return (User) find("firstName",firstName);
    }

    public boolean existsbyUsername(String username){    //in Quarkus, an existsBy query method is not available similarly to Spring Data

           return count("username",username)==1;
    }

    public boolean existsbyEmail(String email){    //in Quarkus, an existsBy query method is not available similarly to Spring Data

        return count("email",email)==1;
    }




}
