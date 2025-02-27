/**
 * Created By Amine Barguellil
 * Date : 2/21/2024
 * Time : 10:49 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.service.impl;

import com.scheidbachmann.masterdata.dto.*;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.entity.Sensor;
import com.scheidbachmann.masterdata.enums.KafkaTopics;
import com.scheidbachmann.masterdata.exceptions.SerialNumberAndLaneNumberAlreadyExistsException;
//import com.scheidbachmann.masterdata.kafka.config.ConnectionPointToTrxMgr.ConnectionPointToTrxMgrKafka;
//import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
//import com.scheidbachmann.masterdata.kafka.config.Sensor.SensorKafka;
import com.scheidbachmann.masterdata.kafka.config.ConnectionPointToTrxMgr.ConnectionPointToTrxMgrSchema;
import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.kafka.config.Sensor.SensorSchema;
import com.scheidbachmann.masterdata.mapper.SensorMapper;
import com.scheidbachmann.masterdata.repository.ConnectionPointRepository;
import com.scheidbachmann.masterdata.repository.SensorRepository;
import com.scheidbachmann.masterdata.repository.impl.SensorRepositoryImpl;
import com.scheidbachmann.masterdata.service.SensorsService;
import com.scheidbachmann.masterdata.utils.ApiKeyGenerator;
import jakarta.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SensorsServiceImpl implements SensorsService {


  private final SensorRepository sensorRepository;

  private final SensorMapper sensorMapper;

  private final SensorRepositoryImpl repositoryQuery;

  private final ConnectionPointRepository connectionPointRepository;

  public static final int DEFAULT_PAGE_SIZE = 35000;
  public static final int MAX_PAGE_SIZE = 3500;

//  private final EventListenerComponent<SensorKafka> eventListenerComponent;
//
//  private final EventListenerComponent<ConnectionPointToTrxMgrKafka> eventListenerComponent1;

  private final EventListenerComponent<KafkaSchema<SensorSchema>> eventListenerComponent;



  private final EventListenerComponent<KafkaSchema<ConnectionPointToTrxMgrSchema>> eventListenerComponent1;

  @Value("${with-sensor-api-key}")
  private Boolean withSensorApiKey;




  public SensorsServiceImpl(SensorRepository sensorRepository, SensorMapper sensorMapper, SensorRepositoryImpl repositoryQuery, ConnectionPointRepository connectionPointRepository, EventListenerComponent<KafkaSchema<SensorSchema>> eventListenerComponent, EventListenerComponent<KafkaSchema<ConnectionPointToTrxMgrSchema>> eventListenerComponent1) {
    this.sensorRepository = sensorRepository;
    this.sensorMapper = sensorMapper;
    this.repositoryQuery = repositoryQuery;
    this.connectionPointRepository = connectionPointRepository;
    this.eventListenerComponent = eventListenerComponent;
    this.eventListenerComponent1 = eventListenerComponent1;
  }


  @Override
  public Page<SensorSearchResult> getSensors(Map<String, Object> searchParams, Pageable page) {
    Page<Sensor> partners = repositoryQuery.search(searchParams, page);
    return partners.map(sensorMapper::toSearchResult);
  }


  @Override
  public void sendConnectionPointCreatedToTrxMgr(ConnectionPointToTrxMgr connectionPointToTrxMgr) {

    ConnectionPointToTrxMgrSchema connectionPointToTrxMgrSchema=new ConnectionPointToTrxMgrSchema();

    BeanUtils.copyProperties(connectionPointToTrxMgr,connectionPointToTrxMgrSchema);

    KafkaSchema<ConnectionPointToTrxMgrSchema> schema = KafkaSchema.with(connectionPointToTrxMgrSchema, connectionPointToTrxMgrSchema.getTenant(), "CONNECTION_POINT_CREATED");
    eventListenerComponent1.sendToTopic(KafkaTopics.CONNECTION_POINT_TOPIC, schema);

  }

  @Override
  public void sendConnectionPointChangedToTrxMgr(ConnectionPointToTrxMgr connectionPointToTrxMgr) {

    ConnectionPointToTrxMgrSchema connectionPointToTrxMgrSchema=new ConnectionPointToTrxMgrSchema();

    BeanUtils.copyProperties(connectionPointToTrxMgr,connectionPointToTrxMgrSchema);

    KafkaSchema<ConnectionPointToTrxMgrSchema> schema = KafkaSchema.with(connectionPointToTrxMgrSchema, connectionPointToTrxMgrSchema.getTenant(), "CONNECTION_POINT_CHANGED");
    eventListenerComponent1.sendToTopic(KafkaTopics.CONNECTION_POINT_TOPIC, schema);

  }

  @Override
  public Page<SensorDto> getSensorsList(Map<String, Object> searchParams, Pageable page) {
    Page<Sensor> partners = repositoryQuery.search(searchParams, page);
    return partners.map(sensorMapper::toDto);
  }

  @Override
  public SensorDto getSensorById(String id) {
    return sensorMapper.toDto(sensorRepository.findById(id).orElse(null));
  }

  @Override
  public String checkUnique(String sensorId, String serialNumber, String laneNumber ) {
    try {

      final Optional<Sensor> existingSensor =
              sensorRepository.findById(sensorId);


      if (serialNumber == null || laneNumber == null) {
        throw SerialNumberAndLaneNumberAlreadyExistsException.with(serialNumber, laneNumber,sensorRepository ,sensorId,this);
      }

      List<String> serialNumbers = getSerialNumbers();
      serialNumbers.remove(existingSensor.map(Sensor::getSerialNumber).orElse(null));

      if (serialNumbers.contains(serialNumber)) {
        throw SerialNumberAndLaneNumberAlreadyExistsException.with(serialNumber, laneNumber, sensorRepository ,sensorId , this);
      }
      return "Sensor is unique.";
    } catch (SerialNumberAndLaneNumberAlreadyExistsException e) {
      return e.getMessage();
    }
  }



  @Override
  @Transactional
  public SensorDto add(SensorDto sensorDto) {
    Sensor sensor = sensorMapper.toEntity(sensorDto);

    // Find the ConnectionPoint
    Optional<ConnectionPoint> optionalConnectionPoint = connectionPointRepository.findById(sensor.getConnectionPointId());
    if (optionalConnectionPoint.isPresent()) {
      ConnectionPoint connectionPoint = optionalConnectionPoint.get();

      // Update the bidirectional relationship
      connectionPoint.getSensors().add(sensor);
      sensor.setConnectionPoint(connectionPoint);

      // Save both entities within the same transaction
      ConnectionPoint addedConnectionPoint = connectionPointRepository.save(connectionPoint);

      System.out.print("////////////" + connectionPoint.getSensors() + "////////////\n");

      sensor = sensorRepository.save(sensor);
    }


    SensorDto sensorDto1=sensorMapper.toDto(sensor);

    SensorSchema sensorSchema=new SensorSchema();

    BeanUtils.copyProperties(sensorDto1,sensorSchema);

    KafkaSchema<SensorSchema> schema = KafkaSchema.with(sensorSchema, sensorDto1.getTenantName(), sensor.getClass().getSimpleName()+"Created");
    eventListenerComponent.sendToTopic(KafkaTopics.SENSOR_TOPIC, schema);




    return sensorMapper.toDto(sensor);
  }




  @Override
  public SensorDto updateSensor(SensorDto sensor) {
    if (!sensorRepository.existsById(sensor.getId())) {
      return add(sensor);
    }
    checkUniqueSensor(sensor.getId() , sensor.getSerialNumber(), sensor.getLaneNumber());
    final Sensor sensor1 =
      sensorRepository.save(sensorMapper.toEntity(sensor));


    final SensorDto sensorDto = sensorMapper.toDto(sensor1);

    SensorSchema sensorSchema=new SensorSchema();

    BeanUtils.copyProperties(sensorDto,sensorSchema);

    KafkaSchema<SensorSchema> schema = KafkaSchema.with(sensorSchema, sensorDto.getTenantName(), sensor1.getClass().getSimpleName()+"Changed");
    eventListenerComponent.sendToTopic(KafkaTopics.SENSOR_TOPIC, schema);

    return sensorDto;
  }

  @Override
  public void deleteSensors(List<String>ids) {
    List<Sensor> sensors = sensorRepository.findAllById(ids);
    for (Sensor sensor : sensors) {
      // Update deletedAt field to current dateTime
      sensor.setDeletedAt(LocalDateTime.now());

      SensorDto sensorDto=sensorMapper.toDto(sensor);

      SensorSchema sensorSchema=new SensorSchema();

      BeanUtils.copyProperties(sensorDto,sensorSchema);

      KafkaSchema<SensorSchema> schema = KafkaSchema.with(sensorSchema, sensorDto.getTenantName(), sensor.getClass().getSimpleName()+"Deleted");
      eventListenerComponent.sendToTopic(KafkaTopics.SENSOR_TOPIC, schema);


    }
    // Save or update the sensors
    sensorRepository.saveAll(sensors);
  }

  private void checkUniqueSensor( String id , String serialNumber, String laneNumber) {
    final Optional<Sensor> existingSensor =
            sensorRepository.findById(id);
    if (serialNumber == null || laneNumber == null) {
      throw SerialNumberAndLaneNumberAlreadyExistsException.with(serialNumber, laneNumber,sensorRepository ,id,this);
    }

    List<String> serialNumbers = getSerialNumbers();
    serialNumbers.remove(existingSensor.map(Sensor::getSerialNumber).orElse(null));

    if (serialNumbers.contains(serialNumber)) {
      throw SerialNumberAndLaneNumberAlreadyExistsException.with(serialNumber, laneNumber, sensorRepository ,id , this);
    }

//    else if (sensorRepository.existsByLaneNumberAndDirectionAndIdNot(laneNumber, directionEnum, id)) {
//      throw SerialNumberAndLaneNumberAlreadyExistsException.with(serialNumber, laneNumber, sensorRepository, directionEnum, id, this);
//    }

  }


  @Override
  public List<String> getSerialNumbers() {
    List<Sensor> Sensors = sensorRepository.findAll().stream()
            .filter(sensor -> sensor.getDeletedAt() == null)
            .toList();


    List<String> serialNumbers = Sensors.stream()
            .map(Sensor::getSerialNumber)
            .collect(Collectors.toList());

    return serialNumbers;
  }

  @Override
  public Boolean getWithSensorApiKey(){
    return this.withSensorApiKey;
  }
  @Override
  public String generateApiKey() {
    return ApiKeyGenerator.generateApiKey();
  }



  @Override
  public Page<SensorExport> getSensorList(Map<String, Object> searchParams, Pageable page) {
    Page<com.scheidbachmann.masterdata.entity.Sensor> sensors = repositoryQuery.search(searchParams, page);
    return sensors.map(sensorMapper::toExport);
  }

}
