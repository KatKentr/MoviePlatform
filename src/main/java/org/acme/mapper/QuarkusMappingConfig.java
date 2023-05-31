package org.acme.mapper;

import org.mapstruct.MapperConfig;

@MapperConfig(componentModel="jakarta")    //encountered errors with componentModel="cdi" Solution from: https://github.com/quarkusio/quarkus/issues/32983
interface QuarkusMappingConfig {
}
