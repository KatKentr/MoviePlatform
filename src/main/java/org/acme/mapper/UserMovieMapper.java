package org.acme.mapper;


import org.acme.dto.MovieDto;
import org.acme.dto.UserMovieDto;
import org.acme.model.Movie;
import org.acme.model.UserMovie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config=QuarkusMappingConfig.class)
public interface UserMovieMapper {


    //@Mapping(target="user",ignore=true)
    UserMovieDto toDto(UserMovie userMovie);


    UserMovie toEntity(UserMovieDto userMovieDto);



}
