package org.acme.mapper;


import org.acme.dto.UserDto;
import org.acme.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config=QuarkusMappingConfig.class)
public interface UserMapper {


    //DAO to DTO
    //@Mapping(target="password",ignore=true) //ignore this value of this field in the mapping (password is null), to don't expose these information to the client. Normally we would have a UserDto without the password field, and a SignupDTO and loginDTO with a password field
    UserDto toDTO(User user);

    //DTO to DAO

    @Mapping(target="id",ignore=true)
    User toEntity(UserDto userDto);




}
