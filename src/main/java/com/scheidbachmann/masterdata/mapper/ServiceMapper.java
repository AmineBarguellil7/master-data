package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.dto.ServiceDto;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author KaouechHaythem
 */
@Mapper(componentModel = "spring")
public interface ServiceMapper extends EntityMapper<ServiceDto, Service> {
  @Override
  @Mapping(target = "connectionPointServices", ignore = true)
  @Mapping(target = "contracts", ignore = true)
  Service toEntity(ServiceDto serviceDto);
}
