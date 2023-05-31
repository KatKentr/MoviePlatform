package org.acme.mapper;


import org.acme.dto.MovieDto;
import org.acme.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config=QuarkusMappingConfig.class)
public interface MovieMapper {

    MovieDto toDto(Movie movie);

    @Mapping(target="id", ignore = true)
    Movie toEntity(MovieDto movieDto);



}
