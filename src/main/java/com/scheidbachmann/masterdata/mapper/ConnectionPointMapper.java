package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.BusinessPartnerExport;
import com.scheidbachmann.masterdata.dto.ConnectionPointDto;
import com.scheidbachmann.masterdata.dto.ConnectionPointExport;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

/**
 * @author KaouechHaythem
 */
@Mapper(componentModel = "spring", uses = {ConnectionPointConnectivityMapper.class,ConnectionPointServiceMapper.class , SensorMapper.class})

public interface ConnectionPointMapper extends EntityMapper<ConnectionPointDto, ConnectionPoint> {
  @Override
  @Mapping(target = "businessPartner", ignore = true)
  @Mapping(target = "consumerContracts", ignore = true)
  @Mapping(target = "supplierContracts", ignore = true)
  ConnectionPointDto toDto(ConnectionPoint connectionPoint);
  @Override
  @Mapping(target = "connectionPointConnectivity", ignore = true)
  @Mapping(target = "connectionPointServices", ignore = true)
  @Mapping(target = "sensors", ignore = true)
  ConnectionPoint toEntity(ConnectionPointDto connectionPointDto);


  Set<ConnectionPointDto> toDtos(Set<ConnectionPoint> connectionPoints);



  @Mapping(target = "id", source = "connectionPoint.id")
  @Mapping(target = "revision", source = "connectionPoint.revision")
  @Mapping(target = "type", source = "connectionPoint.type")
  @Mapping(target = "name", source = "connectionPoint.name")
  @Mapping(target = "locationId", source = "connectionPoint.locationId")
  @Mapping(target = "facilityId", source = "connectionPoint.facilityId")
  @Mapping(target = "cellId", source = "connectionPoint.cellId")
  @Mapping(target = "operatorId", source = "connectionPoint.operatorId")
  @Mapping(target = "carparkType", source = "connectionPoint.carparkType")
  @Mapping(target = "lastModified", source = "connectionPoint.lastModified")
  @Mapping(target = "orderNumber", source = "connectionPoint.orderNumber")
  @Mapping(target = "technicalPlace", source = "connectionPoint.technicalPlace")
  @Mapping(target = "activatedAt", source = "connectionPoint.activatedAt")
  @Mapping(target = "other", source = "connectionPoint.other")
  @Mapping(target = "withLeaveLoop", source = "connectionPoint.withLeaveLoop")
  @Mapping(target = "tenantName", source = "connectionPoint.tenantName")
  @Mapping(target = "geometryPath", source = "connectionPoint.geometryPath")
  @Mapping(target = "keycloakInboundUser", source = "connectionPoint.keycloakInboundUser")
  @Mapping(target = "businessPartnerId", source = "connectionPoint.businessPartnerId")
  ConnectionPointExport toExport(ConnectionPoint connectionPoint);



}
