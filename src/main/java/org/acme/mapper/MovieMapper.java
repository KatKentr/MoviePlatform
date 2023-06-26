package org.acme.mapper;


import org.acme.dto.MovieDto;
import org.acme.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config=QuarkusMappingConfig.class)
public interface MovieMapper {

    @Mapping(target = "rating", source = "average")
    MovieDto toDto(Movie movie);

    @Mapping(target="id", ignore = true)
    @Mapping(target="average",source="rating",ignore = true)
    Movie toEntity(MovieDto movieDto);



}
