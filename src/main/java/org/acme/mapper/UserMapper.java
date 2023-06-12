package org.acme.mapper;


import org.acme.dto.SignupDto;
import org.acme.dto.UserDto;
import org.acme.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config=QuarkusMappingConfig.class)
public interface UserMapper {


    //DAO to DTO

    //@Mapping(target="movies",ignore=true)   //ignore this value of this field in the mapping (movies is null)
    UserDto toDTO(User user);

    //DTO to DAO

    @Mapping(target="id",ignore=true)   //used for updating a user, and adding following users
//    @Mapping(target="movies",ignore=true)
 //   @Mapping(target="role",ignore=true)
//    @Mapping(target="email",ignore=true)
    User toEntity(UserDto userDto);


    User fromSignUpToUser(SignupDto SignupDto);     //registering a user

}
