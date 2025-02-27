package com.scheidbachmann.masterdata.mapper;


import com.scheidbachmann.masterdata.dto.ContractLicenseDto;
import com.scheidbachmann.masterdata.entity.ContractLicense;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractLicenseMapper extends EntityMapper<ContractLicenseDto, ContractLicense>  {

}
