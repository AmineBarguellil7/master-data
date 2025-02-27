package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.ConnectionPointDto;
import com.scheidbachmann.masterdata.dto.ConnectionPointServiceDto;
import com.scheidbachmann.masterdata.dto.PersonDto;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.entity.ConnectionPointService;
import com.scheidbachmann.masterdata.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

/**
 * @author KaouechHaythem
 */
@Mapper(componentModel = "spring")
public interface ConnectionPointServiceMapper extends EntityMapper<ConnectionPointServiceDto, ConnectionPointService> {

  @Override
  @Mapping(target = "service", ignore = true)
  @Mapping(target = "connectionPoint", ignore = true)
  ConnectionPointServiceDto toDto(ConnectionPointService connectionPointService);

  Set<ConnectionPointServiceDto> toDtos(Set<ConnectionPointService> connectionPointServices);
  Set<ConnectionPointService> toEntities(Set<ConnectionPointServiceDto> connectionPointServiceDtos);

}
