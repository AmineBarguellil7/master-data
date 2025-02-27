package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.ConnectionPointConnectivityDto;
import com.scheidbachmann.masterdata.dto.ConnectionPointDto;
import com.scheidbachmann.masterdata.entity.ConnectionPointConnectivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

/**
 * @author KaouechHaythem
 */
@Mapper(componentModel = "spring", uses = {InboundConnectivityCredentialsMapper.class,OutboundConnectivityCredentialsMapper.class})
public interface ConnectionPointConnectivityMapper extends EntityMapper<ConnectionPointConnectivityDto,ConnectionPointConnectivity>{
  @Override
  @Mapping(target = "connectionPoint", ignore = true)
  ConnectionPointConnectivityDto toDto(ConnectionPointConnectivity connectionPointConnectivity);

  ConnectionPointConnectivity toEntity (ConnectionPointConnectivityDto connectionPointConnectivity );
}
