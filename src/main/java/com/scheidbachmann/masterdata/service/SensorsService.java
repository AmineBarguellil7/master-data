package com.scheidbachmann.masterdata.service;


import com.scheidbachmann.masterdata.dto.*;
import com.scheidbachmann.masterdata.enums.DirectionEnum;
import com.scheidbachmann.masterdata.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SensorsService {
  void deleteSensors(List<String> ids);

  SensorDto add(SensorDto sensorDto);

  SensorDto updateSensor(SensorDto sensor);


  Page<SensorSearchResult> getSensors(Map<String, Object> searchParams, Pageable page);

  void sendConnectionPointCreatedToTrxMgr(
          ConnectionPointToTrxMgr connectionPointToTrxMgr);

  void sendConnectionPointChangedToTrxMgr(
          ConnectionPointToTrxMgr connectionPointToTrxMgr);

  Page<SensorDto> getSensorsList(Map<String, Object> searchParams, Pageable page);

  SensorDto getSensorById(String id);


  String checkUnique(String sensorId, String serialNumber, String laneNumber );

  List<String> getSerialNumbers();

    Boolean getWithSensorApiKey();

    String generateApiKey();

//  List<DirectionEnum>  getSensorsDirections();


  Page<SensorExport> getSensorList(Map<String, Object> searchParams, Pageable page);


}
