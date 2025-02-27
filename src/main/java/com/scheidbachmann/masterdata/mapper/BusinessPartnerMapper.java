package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.dto.BusinessPartnerExport;
import com.scheidbachmann.masterdata.dto.BusinessPartnerNameId;
import com.scheidbachmann.masterdata.dto.BusinessPartnerSearchResult;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;


@Mapper(componentModel = "spring", uses = {PersonMapper.class,ConnectionPointMapper.class,ContractMapper.class})
public interface BusinessPartnerMapper extends EntityMapper<BusinessPartnerDto, BusinessPartner> {
  @Override
  @Mapping(target = "contactPerson", ignore = true)
  @Mapping(target = "connectionPoints", ignore = true)
  @Mapping(target = "consumerContracts", ignore = true)
  @Mapping(target = "supplierContracts", ignore = true)
  BusinessPartner toEntity(BusinessPartnerDto businessPartnerDto);


  @Mapping(target = "id", source = "businessPartner.id")
  @Mapping(target = "partnerNumber", source = "businessPartner.partnerNumber")
  @Mapping(target = "name", source = "businessPartner.name")
  @Mapping(target = "countryCode", source = "businessPartner.countryCode")
  @Mapping(target = "city", source = "businessPartner.city")
  BusinessPartnerSearchResult toSearchResult(BusinessPartner businessPartner);


  @Mapping(target = "id", source = "businessPartner.id")
  @Mapping(target = "name", source = "businessPartner.name")
  Set<BusinessPartnerNameId> toNameIds(Set<BusinessPartner> businessPartners);


  @Mapping(target = "id", source = "businessPartner.id")
  @Mapping(target = "partnerNumber", source = "businessPartner.partnerNumber")
  @Mapping(target = "providerId", source = "businessPartner.providerId")
  @Mapping(target = "tenantId", source = "businessPartner.tenantId")
  @Mapping(target = "name", source = "businessPartner.name")
  @Mapping(target = "countryCode", source = "businessPartner.countryCode")
  @Mapping(target = "city", source = "businessPartner.city")
  @Mapping(target = "currency", source = "businessPartner.currency")
  @Mapping(target = "switchOffExit", source = "businessPartner.switchOffExit")
  @Mapping(target = "type", source = "businessPartner.type")
  @Mapping(target = "revision", source = "businessPartner.revision")
  @Mapping(target = "contactPerson", source = "businessPartner.contactPerson")
  BusinessPartnerExport toExport(BusinessPartner businessPartner);

}
