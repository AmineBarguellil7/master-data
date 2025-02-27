/**
 * Created By Amine Barguellil
 * Date : 2/21/2024
 * Time : 11:00 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.service.impl;



import com.scheidbachmann.masterdata.dto.*;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.entity.Contract;
import com.scheidbachmann.masterdata.enums.KafkaTopics;
import com.scheidbachmann.masterdata.enums.LicenseType;
//import com.scheidbachmann.masterdata.kafka.config.BusinessPartner.BusinessPartnerKafka;
//import com.scheidbachmann.masterdata.kafka.config.Contract.ContractKafka;
import com.scheidbachmann.masterdata.exceptions.ContractEndBeforeContractStartException;
import com.scheidbachmann.masterdata.kafka.config.BusinessPartner.BusinessPartnerSchema;
import com.scheidbachmann.masterdata.kafka.config.Contract.ContractSchema;
import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.mapper.ConnectionPointMapper;
import com.scheidbachmann.masterdata.mapper.ContractMapper;
import com.scheidbachmann.masterdata.repository.ConnectionPointRepository;
import com.scheidbachmann.masterdata.repository.ContractRepository;
import com.scheidbachmann.masterdata.repository.ServiceRepository;
import com.scheidbachmann.masterdata.repository.impl.ContractsRepositoryImpl;
import com.scheidbachmann.masterdata.service.ConnectionPointsService;
import com.scheidbachmann.masterdata.service.ContractsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ContractsServiceImpl implements ContractsService {

  private final ContractMapper contractMapper;

  private final ContractRepository contractRepository;

  private final ServiceRepository serviceRepository;

  private final ConnectionPointsService connectionPointsService;
  private final ContractsRepositoryImpl repositoryQuery;

  private final ConnectionPointMapper connectionPointMapper;

  private final ConnectionPointRepository connectionPointRepository;


//  private final EventListenerComponent<ContractKafka> eventListenerComponent;

  private final EventListenerComponent<KafkaSchema<ContractSchema>> eventListenerComponent;



  public ContractsServiceImpl(ContractMapper contractMapper, ContractRepository contractRepository, ServiceRepository serviceRepository, ConnectionPointsService connectionPointsService, ContractsRepositoryImpl repositoryQuery, ConnectionPointMapper connectionPointMapper, ConnectionPointRepository connectionPointRepository, EventListenerComponent<KafkaSchema<ContractSchema>> eventListenerComponent) {
    this.contractMapper = contractMapper;
    this.contractRepository = contractRepository;
    this.serviceRepository = serviceRepository;
    this.connectionPointsService = connectionPointsService;
    this.repositoryQuery = repositoryQuery;
    this.connectionPointMapper = connectionPointMapper;
    this.connectionPointRepository = connectionPointRepository;
//    this.eventListenerComponent = eventListenerComponent;
    this.eventListenerComponent = eventListenerComponent;
  }

  @Override
  public Page<ContractSearchResult> getContracts(Map<String, Object> searchParams, Pageable page) {
    Page<Contract> contracts = repositoryQuery.search(searchParams, page);
    return contracts.map(contractMapper::toSearchResult);
  }

  @Override
  public ContractDto updateContract(ContractDto contractDto) {


    if (contractDto.getContractEnd()!=null && contractDto.getContractEnd().isBefore(contractDto.getContractStart())){

   throw new ContractEndBeforeContractStartException("Contract End must be AFTER Contract Start");
 }

    final Contract entity =
      contractMapper.toEntity(contractDto);

    List<ConnectionPointSearchResult> supplierConnectionPointSearchResults = connectionPointsService.getConnectionPointsByBPId(contractDto.getSupplierId());

    Set<String> supplierConnectionPointsIds = contractDto.getSupplierConnectionPointsIds();



    if (supplierConnectionPointsIds != null) {
      Set<String> supplierConnectionPointsIdsList = new HashSet<>();

      for (String Id : supplierConnectionPointsIds) {
        for (ConnectionPointSearchResult result : supplierConnectionPointSearchResults) {
          if (result.getId().equals(Id)) {
            supplierConnectionPointsIdsList.add(Id);
            break;
          }
        }
      }



      Set<ConnectionPointDto> supplierConnectionPoints = new HashSet<>();

      for (String Id : supplierConnectionPointsIdsList) {
        ConnectionPointDto connectionPointDto = connectionPointsService.getConnectionPoint(Id);
        if (connectionPointDto != null) {
          supplierConnectionPoints.add(connectionPointDto);
        }
      }

      Set<ConnectionPoint> supplierconnectionPointEntities = supplierConnectionPoints.stream()
              .map(connectionPointMapper::toEntity)
              .collect(Collectors.toSet());
      entity.setSupplierConnectionPoints(supplierconnectionPointEntities);

    }




    List<ConnectionPointSearchResult> consumerConnectionPointSearchResults = connectionPointsService.getConnectionPointsByBPId(contractDto.getConsumerId());

    Set<String> consumerConnectionPointsIds = contractDto.getConsumerConnectionPointsIds();

    if (consumerConnectionPointsIds != null) {
      Set<String> consumerConnectionPointsIdsList = new HashSet<>();

      for (String Id : consumerConnectionPointsIds) {
        for (ConnectionPointSearchResult result : consumerConnectionPointSearchResults) {
          if (result.getId().equals(Id)) {
            consumerConnectionPointsIdsList.add(Id);
            break;
          }
        }
      }

      Set<ConnectionPointDto> consumerConnectionPoints = new HashSet<>();

      for (String Id : consumerConnectionPointsIdsList) {
        ConnectionPointDto connectionPointDto = connectionPointsService.getConnectionPoint(Id);
        if (connectionPointDto != null) {
          consumerConnectionPoints.add(connectionPointDto);
        }
      }

      Set<ConnectionPoint> consumerconnectionPointEntities = consumerConnectionPoints.stream()
              .map(connectionPointMapper::toEntity)
              .collect(Collectors.toSet());
      entity.setConsumerConnectionPoints(consumerconnectionPointEntities);

    }

    Contract contract=contractRepository.save(entity);


    if (supplierConnectionPointsIds != null) {
      contract.setSupplierConnectionPoints(entity.getSupplierConnectionPoints());
    }

    if (consumerConnectionPointsIds != null) {
      contract.setConsumerConnectionPoints(entity.getConsumerConnectionPoints());
    }



    ContractDto contractDto1=contractMapper.toDto(contract);

    if (contractDto1.getSupplierConnectionPoints().isEmpty()) {
      contractDto1.setSupplierConnectionPoints(entity.getSupplierConnectionPoints().stream()
              .map(connectionPointMapper::toDto)
              .collect(Collectors.toSet()));
    }

    if (contractDto1.getConsumerConnectionPoints().isEmpty()) {
      contractDto1.setConsumerConnectionPoints(entity.getConsumerConnectionPoints().stream()
              .map(connectionPointMapper::toDto)
              .collect(Collectors.toSet()));
    }

    if (supplierConnectionPointsIds != null) {
      contractDto1.setSupplierConnectionPointsIds(contractDto.getSupplierConnectionPointsIds());
    }

    System.out.print("///////////" +contractDto1.getSupplierConnectionPointsIds());

    if ( consumerConnectionPointsIds != null) {
      contractDto1.setConsumerConnectionPointsIds(contractDto.getConsumerConnectionPointsIds());
    }


    ContractSchema contractSchema=new ContractSchema();

    BeanUtils.copyProperties(contractDto1,contractSchema);

    KafkaSchema<ContractSchema> schema = KafkaSchema.with(contractSchema, null, entity.getClass().getSimpleName()+"Changed");
    eventListenerComponent.sendToTopic(KafkaTopics.CONTRACT_TOPIC, schema);

    return contractDto1;
  }

  @Override
  public ContractDto getContractById(String id) {
    Optional<Contract> contractOptional = contractRepository.findById(id);
    if (contractOptional.isPresent()) {
      Contract contract = contractOptional.get();
      ContractDto contractDto = contractMapper.toDto(contract);

      Set<ConnectionPointDto> consumerConnectionPointDtos = contract.getConsumerConnectionPoints()
              .stream()
              .map(connectionPointMapper::toDto)
              .collect(Collectors.toSet());
      contractDto.setConsumerConnectionPoints(consumerConnectionPointDtos);



      Set<ConnectionPointDto> supplierConnectionPointDtos = contract.getSupplierConnectionPoints()
              .stream()
              .map(connectionPointMapper::toDto)
              .collect(Collectors.toSet());
      contractDto.setSupplierConnectionPoints(supplierConnectionPointDtos);

      if (contractDto.getConsumerConnectionPoints() != null) {
        contractDto.setConsumerConnectionPointsIds(
                contractDto.getConsumerConnectionPoints()
                        .stream()
                        .map(ConnectionPointDto::getId)
                        .collect(Collectors.toSet())
        );
      }



      if (contractDto.getSupplierConnectionPoints() != null) {
        contractDto.setSupplierConnectionPointsIds(
                contractDto.getSupplierConnectionPoints()
                        .stream()
                        .map(ConnectionPointDto::getId)
                        .collect(Collectors.toSet())
        );
      }
      return contractDto;
    } else {
      return null; // or throw an exception indicating contract not found
    }
  }



  @Override
  public List<com.scheidbachmann.masterdata.entity.Service> getServices() {
    return serviceRepository.findAll();
  }


  @Override
  public ContractDto add(ContractDto contractDto) {

    final Contract entity =
            contractMapper.toEntity(contractDto);

    List<ConnectionPointSearchResult> supplierConnectionPointSearchResults = connectionPointsService.getConnectionPointsByBPId(contractDto.getSupplierId());

    Set<String> supplierConnectionPointsIds = contractDto.getSupplierConnectionPointsIds();
    if (contractDto.getContractEnd()!=null && contractDto.getContractEnd().isBefore(contractDto.getContractStart())){

      throw new ContractEndBeforeContractStartException("Contract End must be AFTER Contract Start");
    }


    if (supplierConnectionPointsIds != null) {
      Set<String> supplierConnectionPointsIdsList = new HashSet<>();

      for (String Id : supplierConnectionPointsIds) {
        for (ConnectionPointSearchResult result : supplierConnectionPointSearchResults) {
          if (result.getId().equals(Id)) {
            supplierConnectionPointsIdsList.add(Id);
            break;
          }
        }
      }



      Set<ConnectionPointDto> supplierConnectionPoints = new HashSet<>();

      for (String Id : supplierConnectionPointsIdsList) {
        ConnectionPointDto connectionPointDto = connectionPointsService.getConnectionPoint(Id);
        if (connectionPointDto != null) {
          supplierConnectionPoints.add(connectionPointDto);
        }
      }

      Set<ConnectionPoint> supplierconnectionPointEntities = supplierConnectionPoints.stream()
              .map(connectionPointMapper::toEntity)
              .collect(Collectors.toSet());
      entity.setSupplierConnectionPoints(supplierconnectionPointEntities);

    }




    List<ConnectionPointSearchResult> consumerConnectionPointSearchResults = connectionPointsService.getConnectionPointsByBPId(contractDto.getConsumerId());

    Set<String> consumerConnectionPointsIds = contractDto.getConsumerConnectionPointsIds();

    if (consumerConnectionPointsIds != null) {
      Set<String> consumerConnectionPointsIdsList = new HashSet<>();

      for (String Id : consumerConnectionPointsIds) {
        for (ConnectionPointSearchResult result : consumerConnectionPointSearchResults) {
          if (result.getId().equals(Id)) {
            consumerConnectionPointsIdsList.add(Id);
            break;
          }
        }
      }

      Set<ConnectionPointDto> consumerConnectionPoints = new HashSet<>();

      for (String Id : consumerConnectionPointsIdsList) {
        ConnectionPointDto connectionPointDto = connectionPointsService.getConnectionPoint(Id);
        if (connectionPointDto != null) {
          consumerConnectionPoints.add(connectionPointDto);
        }
      }

      Set<ConnectionPoint> consumerconnectionPointEntities = consumerConnectionPoints.stream()
              .map(connectionPointMapper::toEntity)
              .collect(Collectors.toSet());
      entity.setConsumerConnectionPoints(consumerconnectionPointEntities);

    }

    Contract contract=contractRepository.save(entity);


    if (supplierConnectionPointsIds != null) {
      contract.setSupplierConnectionPoints(entity.getSupplierConnectionPoints());
    }

    if (consumerConnectionPointsIds != null) {
      contract.setConsumerConnectionPoints(entity.getConsumerConnectionPoints());
    }



    ContractDto contractDto1=contractMapper.toDto(contract);

    if (contractDto1.getSupplierConnectionPoints().isEmpty()) {
      contractDto1.setSupplierConnectionPoints(entity.getSupplierConnectionPoints().stream()
              .map(connectionPointMapper::toDto)
              .collect(Collectors.toSet()));
    }

    if (contractDto1.getConsumerConnectionPoints().isEmpty()) {
      contractDto1.setConsumerConnectionPoints(entity.getConsumerConnectionPoints().stream()
              .map(connectionPointMapper::toDto)
              .collect(Collectors.toSet()));
    }

    if (supplierConnectionPointsIds != null) {
      contractDto1.setSupplierConnectionPointsIds(contractDto.getSupplierConnectionPointsIds());
    }


    if ( consumerConnectionPointsIds != null) {
      contractDto1.setConsumerConnectionPointsIds(contractDto.getConsumerConnectionPointsIds());
    }


    ContractSchema contractSchema=new ContractSchema();

    BeanUtils.copyProperties(contractDto1,contractSchema);

    KafkaSchema<ContractSchema> schema = KafkaSchema.with(contractSchema, null, entity.getClass().getSimpleName()+"Created");
    eventListenerComponent.sendToTopic(KafkaTopics.CONTRACT_TOPIC, schema);


    return contractDto1;

  }


  public List<LicenseType> getAllLicenseTypes() {
    return Arrays.asList(LicenseType.values());
  }

  @Override
  public void deleteContracts(List<String> ids) {
    List<Contract> contracts = contractRepository.findAllById(ids);
    for (Contract contract : contracts) {
        if (!(contract.getContractEnd().isAfter(contract.getContractStart()) && contract.getContractEnd().isAfter(LocalDate.now()))) {
            // Update deletedAt field to current dateTime
            contract.setDeletedAt(LocalDateTime.now());

            ContractDto contractDto=contractMapper.toDto(contract);

            ContractSchema contractSchema=new ContractSchema();

            BeanUtils.copyProperties(contractDto,contractSchema);

            KafkaSchema<ContractSchema> schema = KafkaSchema.with(contractSchema, null, contract.getClass().getSimpleName()+"Deleted");
            eventListenerComponent.sendToTopic(KafkaTopics.CONTRACT_TOPIC, schema);

        }
        else {
            throw new IllegalStateException("Contract is still valid.");
        }


    }
    // Save or update the sensors
    contractRepository.saveAll(contracts);
  }


    @Override
    public Page<ContractExport> getContractsList(Map<String, Object> searchParams, Pageable page) {
        Page<com.scheidbachmann.masterdata.entity.Contract> contracts = repositoryQuery.search(searchParams, page);
        return contracts.map(contractMapper::toExport);
    }



  }



