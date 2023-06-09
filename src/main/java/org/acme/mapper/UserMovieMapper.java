package org.acme.mapper;


import org.acme.dto.MovieDto;
import org.acme.dto.UserMovieDto;
import org.acme.model.Movie;
import org.acme.model.User;
import org.acme.model.UserMovie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config=QuarkusMappingConfig.class)
public interface UserMovieMapper {


    //we aggregate fields from two source classes into a single dto
    @Mapping(source="user.username",target="username")
    @Mapping(source="user.email",target="email")
    UserMovieDto toDto(UserMovie userMovie, User user);


    UserMovie toEntity(UserMovieDto userMovieDto);



}
