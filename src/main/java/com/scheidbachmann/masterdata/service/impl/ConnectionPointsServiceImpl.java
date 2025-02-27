/**
 * Created By Amine Barguellil
 * Date : 2/21/2024
 * Time : 10:31 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.service.impl;

import com.scheidbachmann.masterdata.dto.*;
import com.scheidbachmann.masterdata.entity.*;
import com.scheidbachmann.masterdata.enums.AuthTypeEnum;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import com.scheidbachmann.masterdata.enums.KafkaTopics;
import com.scheidbachmann.masterdata.kafka.config.ConnectionPoint.ConnectionPointSchema;
import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.mapper.ConnectionPointConnectivityMapper;
import com.scheidbachmann.masterdata.mapper.ConnectionPointMapper;
import com.scheidbachmann.masterdata.mapper.ConnectionPointServiceMapper;
import com.scheidbachmann.masterdata.mapper.ServiceMapper;
import com.scheidbachmann.masterdata.mapper.impl.ConnectionPointToConnectionPointSearchResult;
import com.scheidbachmann.masterdata.repository.*;
import com.scheidbachmann.masterdata.repository.impl.ConnectionPointRepositoryImpl;
import com.scheidbachmann.masterdata.service.ConnectionPointsService;
import com.scheidbachmann.masterdata.service.IamService;
import com.scheidbachmann.masterdata.service.SensorsService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


import static com.scheidbachmann.masterdata.utils.Constants.*;


@Service
@Slf4j
public class ConnectionPointsServiceImpl implements ConnectionPointsService {

  private final ConnectionPointRepository connectionPointRepository;

  private final ConnectionPointConnectivityMapper connectionPointConnectivityMapper;

  private final ConnectionPointConnectivityRepository connectionPointConnectivityRepository;

  private final SensorRepository sensorRepository;

  private final SensorsService sensorsService;

  private final ServiceRepository serviceRepository;

  private final ServiceMapper serviceMapper;

  private final ConnectionPointMapper connectionPointMapper;

  private final ConnectionPointServiceRepository connectionPointServiceRepository;

  private final ConnectionPointServiceMapper connectionPointServiceMapper;

  private final ConnectionPointRepositoryImpl repositoryQuery;
  private final IamService iamService;
  private String inboundUsername;

  private String inboundPassword;

  private final ConnectionPointToConnectionPointSearchResult connectionPointToConnectionPointSearchResult;



  private final EventListenerComponent<KafkaSchema<ConnectionPointSchema>> eventListenerComponent;

  private final ContractRepository contractRepository;

  public ConnectionPointsServiceImpl(ConnectionPointRepository connectionPointRepository, ConnectionPointMapper connectionPointMapper, ConnectionPointServiceMapper connectionPointServiceMapper, ConnectionPointServiceRepository connectionPointServiceRepository, ConnectionPointConnectivityMapper connectionPointConnectivityMapper, ConnectionPointConnectivityRepository connectionPointConnectivityRepository, SensorRepository sensorRepository, SensorsService sensorsService, ServiceRepository serviceRepository, ServiceMapper serviceMapper, ConnectionPointServiceRepository connectionPointServiceRepository1, ConnectionPointServiceMapper connectionPointServiceMapper1, ConnectionPointRepositoryImpl repositoryQuery, IamService iamService, ConnectionPointToConnectionPointSearchResult connectionPointToConnectionPointSearchResult, EventListenerComponent<KafkaSchema<ConnectionPointSchema>> eventListenerComponent, ContractRepository contractRepository) {
    this.connectionPointRepository = connectionPointRepository;
    this.connectionPointMapper = connectionPointMapper;
    this.connectionPointConnectivityMapper = connectionPointConnectivityMapper;
    this.connectionPointConnectivityRepository = connectionPointConnectivityRepository;
      this.sensorRepository = sensorRepository;
      this.sensorsService = sensorsService;
      this.serviceRepository = serviceRepository;
    this.serviceMapper = serviceMapper;
    this.connectionPointServiceRepository = connectionPointServiceRepository1;
    this.connectionPointServiceMapper = connectionPointServiceMapper1;
    this.repositoryQuery = repositoryQuery;
    this.iamService = iamService;
      this.connectionPointToConnectionPointSearchResult = connectionPointToConnectionPointSearchResult;
    this.eventListenerComponent = eventListenerComponent;
    this.contractRepository = contractRepository;
  }

  @Override
  public Page<ConnectionPointSearchResult> getConnectionPoints(Map<String, Object> searchParams, Pageable page) {
    Page<ConnectionPoint> partners = repositoryQuery.search(searchParams, page);
    return partners.map(connectionPointToConnectionPointSearchResult::toSearchResult);
  }


//  @Override
//  public List<ConnectionPointTypeEnum> getConnectionPointsTypes() {
//    return Arrays.asList(ConnectionPointTypeEnum.values());
//  }

//  @Override
//  public List<CarparkTypeEnum> getCarparkTypes() {
//    return Arrays.asList(CarparkTypeEnum.values());
//  }

  @Override
  public List<ConnectionPointSearchResult> getConnectionPointsByBPId(String BPid) {
    return connectionPointToConnectionPointSearchResult.toSearchResults(connectionPointRepository.getConnectionPointsByBPId(BPid)) ;
  }
  @Override
  public List<ConnectionPointSearchResult> getConnectionPointsByBPIdAndType(String BPid ,String type) {
    return connectionPointToConnectionPointSearchResult.toSearchResults(connectionPointRepository.getConnectionPointsByBPIdAndType(BPid , ConnectionPointTypeEnum.valueOf(type))) ;
  }


//  @Override
//  public List<Map<String, String>> testConnectionPointsByBusinessPartnerName(String name) {
//    return connectionPointRepository.getBusinessPartnersInfoByName(name);
//  }

  @Override
  public ConnectionPointDto getConnectionPoint(String id) {
    return connectionPointRepository
      .findById(id)
      .map(connectionPointMapper::toDto).orElse(null);

  }

  @Override
  @Transactional
  public ConnectionPointDto add(ConnectionPointDto connectionPointDto) {

    if (ConnectionPointTypeEnum.FACILITY.equals(connectionPointDto.getType()) &&
      StringUtils.isNotEmpty(connectionPointDto.getLocationId()) &&
      connectionPointDto.getConnectionPointConnectivity() == null &&
      StringUtils.isNotEmpty(connectionPointDto.getTenantName())) {
      inboundUsername = INBOUND_USERNAME + connectionPointDto.getLocationId().toLowerCase();
      inboundPassword = INBOUND_PASSWORD + connectionPointDto.getLocationId().substring(2, 6) + "$%!";
      createConnectivity(connectionPointDto, inboundUsername);
    }

    final ConnectionPoint
      connectionPointEntity = connectionPointMapper.toEntity(connectionPointDto);
    connectionPointEntity.setKeycloakInboundUser(true);

    log.info("addConnectionPoint {} with locationId {}", connectionPointEntity, connectionPointEntity.getLocationId());

    if(connectionPointDto.getType().toString().equals("FACILITY")){
      Boolean isLocationIdUnique= checkUniqueLocationId(connectionPointDto.getId(),connectionPointDto.getLocationId());
      if (!isLocationIdUnique){
        throw new EntityExistsException("A connection point with locationId " + connectionPointDto.getLocationId() + " already exists");
      }}

    try {

      final ConnectionPoint entity = connectionPointRepository.save(connectionPointEntity);
      //call IAM to create inbound user
      if (StringUtils.isNotEmpty(entity.getTenantName())) {
        try {
          iamService.createUser(entity.getTenantName(), connectionPointEntity.getId().toString(), inboundUsername, inboundPassword);
        } catch (Exception e) {
          entity.setKeycloakInboundUser(false);
          connectionPointRepository.save(entity);

          log.warn("Exception has been occurred while connecting to IAM {} ", e);
        }
      }
      ConnectionPointConnectivity connectionPointConnectivity = connectionPointConnectivityMapper.toEntity(connectionPointDto.getConnectionPointConnectivity());
      if (connectionPointConnectivity != null) {
        connectionPointConnectivity.setConnectionPointId(entity.getId());
        connectionPointConnectivityRepository.save(connectionPointConnectivity);
      }

      Set<ConnectionPointServiceDto> connectionPointServiceDtos = connectionPointDto.getConnectionPointServices();


      if (connectionPointServiceDtos != null) {
        for (ConnectionPointServiceDto serviceDto : connectionPointServiceDtos) {
          ConnectionPointService service = connectionPointServiceMapper.toEntity(serviceDto);
          service.setConnectionPointId(entity.getId());
          connectionPointServiceRepository.save(service);

        }
      }
      final ConnectionPointDto result = connectionPointMapper.toDto(entity);
      result.setConnectionPointConnectivity(connectionPointConnectivityMapper.toDto(connectionPointConnectivity));
      result.setConnectionPointServices(connectionPointServiceDtos);

      ConnectionPoint connectionPoint=connectionPointMapper.toEntity(result);


      ConnectionPointSchema connectionPointSchema=new ConnectionPointSchema();

      BeanUtils.copyProperties(result,connectionPointSchema);

      KafkaSchema<ConnectionPointSchema> schema = KafkaSchema.with(connectionPointSchema, result.getTenantName(), connectionPoint.getClass().getSimpleName()+"Created");
      eventListenerComponent.sendToTopic(KafkaTopics.CONNECTION_POINT_TOPIC, schema);


      return result;
    } catch (Exception e) {
      log.error("exception when saving connection point {}", e);
      throw e;
    }
  }

  private void createConnectivity(ConnectionPointDto connectionPoint, String inboundUsername) {
    ConnectionPointConnectivityDto connectivity = new ConnectionPointConnectivityDto();
    connectivity.setBaseUrl("/");
    connectivity.setHost("localhost");
    connectivity.setPort(8080);
    InboundConnectivityCredentialsDto inboundConnectivityCredentials = new InboundConnectivityCredentialsDto();
    inboundConnectivityCredentials.setInbUserName(inboundUsername);
    OutboundConnectivityCredentialsDto outboundConnectivityCredentials = new OutboundConnectivityCredentialsDto();
    outboundConnectivityCredentials.setAuthType(AuthTypeEnum.BASIC_AUTH);
    connectivity.setInboundCredentials(inboundConnectivityCredentials);
    connectivity.setOutboundCredentials(outboundConnectivityCredentials);
    connectionPoint.setConnectionPointConnectivity(connectivity);
  }

//  @Transactional
  @Override
  public ConnectionPointDto saveConnectionPoint(ConnectionPointDto connectionPointDto) throws InterruptedException {
    Set<Sensor> sensors = connectionPointRepository.findById(connectionPointDto.getId()).get().getSensors();
    ConnectionPoint cpEntity = connectionPointMapper.toEntity(connectionPointDto);
    System.out.print("////////////// ConnectionPoint :" + cpEntity + "\n");
    cpEntity.setSensors(sensors);
    // enforce update even if just detail needs to be saved, to ensure new version in JMS-Message
    cpEntity.updateLastModified();
    if(connectionPointDto.getType().toString().equals("FACILITY")){
    Boolean isLocationIdUnique= checkUniqueLocationId(connectionPointDto.getId(),connectionPointDto.getLocationId());
    if (!isLocationIdUnique){
      throw new EntityExistsException("A connection point with locationId " + connectionPointDto.getLocationId() + " already exists");
    }}
    try {
      cpEntity = connectionPointRepository.save(cpEntity);

      ConnectionPointConnectivity connectionPointConnectivity = connectionPointConnectivityMapper.toEntity(connectionPointDto.getConnectionPointConnectivity());
      if (connectionPointConnectivity != null) {
        connectionPointConnectivity.setConnectionPointId(cpEntity.getId());
       ConnectionPointConnectivity connnectivityEntity=connectionPointConnectivityRepository.save(connectionPointConnectivity);
        System.out.println(connectionPointConnectivity);
        cpEntity.setConnectionPointConnectivity(connnectivityEntity);
      }

      Set<ConnectionPointServiceDto> connectionPointServiceDtos = connectionPointDto.getConnectionPointServices();

      if (connectionPointServiceDtos != null) {
        Set<ConnectionPointService>connectionPointServices = connectionPointServiceMapper.toEntities(connectionPointServiceDtos);
        for (ConnectionPointService service : connectionPointServices) {
          service.setConnectionPointId(cpEntity.getId());
          connectionPointServiceRepository.save(service);
        }
        cpEntity.getConnectionPointServices().clear();
        cpEntity.getConnectionPointServices().addAll(connectionPointServices);

      }

      ConnectionPointDto result = connectionPointMapper.toDto(connectionPointRepository.save(cpEntity)) ;
      result.setConnectionPointConnectivity(connectionPointConnectivityMapper.toDto(connectionPointConnectivity));
      result.setConnectionPointServices(connectionPointServiceDtos);


      ConnectionPoint connectionPoint=connectionPointMapper.toEntity(result);

      ConnectionPointSchema connectionPointSchema=new ConnectionPointSchema();

      BeanUtils.copyProperties(result,connectionPointSchema);

      KafkaSchema<ConnectionPointSchema> schema = KafkaSchema.with(connectionPointSchema, result.getTenantName(), connectionPoint.getClass().getSimpleName()+"Changed");
      eventListenerComponent.sendToTopic(KafkaTopics.CONNECTION_POINT_TOPIC, schema);



      return result;
    } catch (Exception e) {
      log.error("exception when saving connectionpoint {}", e);
      throw e;
    }
  }


  @Override
  public void deleteConnectionPoints(List<String> ids) {
    List<ConnectionPoint> connectionPoints = connectionPointRepository.findAllById(ids);
    for (ConnectionPoint connectionPoint : connectionPoints) {
      String businessPartnerId=connectionPoint.getBusinessPartnerId();


      List<Contract> contracts=contractRepository.findAll();

      for (Contract contract:contracts) {
        if (contract.getConsumerId().equals(businessPartnerId) || contract.getSupplierId().equals(businessPartnerId)) {
          if (!(contract.getContractEnd().isAfter(contract.getContractStart()) && contract.getContractEnd().isAfter(LocalDate.now()))) {
            // Update deletedAt field to current dateTime
            connectionPoint.setDeletedAt(LocalDateTime.now());

            ConnectionPointDto connectionPointDto=connectionPointMapper.toDto(connectionPoint);

            ConnectionPointSchema connectionPointSchema=new ConnectionPointSchema();

            BeanUtils.copyProperties(connectionPointDto,connectionPointSchema);

            KafkaSchema<ConnectionPointSchema> schema = KafkaSchema.with(connectionPointSchema, connectionPointDto.getTenantName(), connectionPoint.getClass().getSimpleName()+"Deleted");
            eventListenerComponent.sendToTopic(KafkaTopics.CONNECTION_POINT_TOPIC, schema);



            // delete all corresponding sensors
            sensorsService.deleteSensors(sensorRepository.getIdsByConnectionPoint(connectionPoint.getId()));

          }
          else {
            throw new IllegalStateException("Contract is still valid.");
          }
        }
      }

    }
    // Save or update the connection points
    connectionPointRepository.saveAll(connectionPoints);
  }



@Override

  /**
   * check unique location id in case of connection point of type facility
   *
   * @param cp the connection point to add or update
   */ public boolean checkUniqueLocationId(String id , String locationId) {
    // check location only for Facility
      if (id == null) {
        log.error("A facility location must not be null ");
      }
      List<ConnectionPoint>
        connections = connectionPointRepository.findAllByLocationId(locationId);
      if (connections.size() > 1 || (connections.size() == 1 && !connections.get(0).getId().equals(id))) {
        log.error("A connection point with locationId {} already exists", locationId);
        return false;  // Location ID is not unique
      }
      return true;

  }


  @Override
  public Page<ConnectionPointExport> getConnectionPointsList(Map<String, Object> searchParams, Pageable page) {
    Page<com.scheidbachmann.masterdata.entity.ConnectionPoint> connectionPoints = repositoryQuery.search(searchParams, page);
    return connectionPoints.map(connectionPointMapper::toExport);
  }





}
