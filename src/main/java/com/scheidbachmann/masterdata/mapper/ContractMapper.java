package com.scheidbachmann.masterdata.mapper;



import com.scheidbachmann.masterdata.dto.*;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.entity.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring", uses = {ContractLicenseMapper.class,ConnectionPointMapper.class})
public interface ContractMapper extends EntityMapper<ContractDto, Contract> {
    @Override
    @Mapping(target = "consumer", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "consumerConnectionPoints", ignore = true)
    @Mapping(target = "supplierConnectionPoints", ignore = true)
    ContractDto toDto(Contract contract);



  @Override
  @Mapping(target = "consumerConnectionPoints", ignore = true)
  @Mapping(target = "supplierConnectionPoints", ignore = true)
  Contract toEntity(ContractDto contractDto);

  @Mapping(target = "id", source = "contract.id")
  @Mapping(target = "supplierId", source = "contract.supplierId")
  @Mapping(target = "supplierName", source = "contract.supplier.name")
  @Mapping(target = "consumerId", source = "contract.consumerId")
  @Mapping(target = "consumerName", source = "contract.consumer.name")
  @Mapping(target = "serviceName", source = "contract.serviceName")
  @Mapping(target = "contractStart", source = "contract.contractStart")
  @Mapping(target = "contractEnd", source = "contract.contractEnd")
  ContractSearchResult toSearchResult(Contract contract);






    @Mapping(target = "id", source = "contract.id")
    @Mapping(target = "revision", source = "contract.revision")
    @Mapping(target = "priorityLevel", source = "contract.priorityLevel")
    @Mapping(target = "supplierConnectionPointSelection", source = "contract.supplierConnectionPointSelection")
    @Mapping(target = "consumerConnectionPointSelection", source = "contract.consumerConnectionPointSelection")
    @Mapping(target = "contractStart", source = "contract.contractStart")
    @Mapping(target = "contractEnd", source = "contract.contractEnd")
    @Mapping(target = "serviceName", source = "contract.serviceName")
    @Mapping(target = "serviceId", source = "contract.serviceId")
    @Mapping(target = "consumerId", source = "contract.consumerId")
    @Mapping(target = "supplierId", source = "contract.supplierId")
    ContractExport toExport(Contract contract);


}
