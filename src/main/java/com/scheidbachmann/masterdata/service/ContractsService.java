package com.scheidbachmann.masterdata.service;

import com.scheidbachmann.masterdata.dto.ContractDto;
import com.scheidbachmann.masterdata.dto.ContractExport;
import com.scheidbachmann.masterdata.dto.ContractSearchResult;
import com.scheidbachmann.masterdata.entity.Service;
import com.scheidbachmann.masterdata.enums.LicenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ContractsService {


  Page<ContractSearchResult> getContracts(Map<String, Object> searchParams, Pageable page);

    ContractDto updateContract(ContractDto contractDto);

  ContractDto getContractById(String id);

//  void deleteContract(String id);

  /* ContractDto add(ContractDto contractDto);*/

  List<Service> getServices();

  ContractDto add(ContractDto contractDto);

  List<LicenseType> getAllLicenseTypes();

  void deleteContracts(List<String>ids);

  Page<ContractExport> getContractsList(Map<String, Object> searchParams, Pageable page);


}
