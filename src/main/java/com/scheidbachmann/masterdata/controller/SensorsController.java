/**
 * Created By Amine Barguellil
 * Date : 2/21/2024
 * Time : 10:52 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.controller;


import com.scheidbachmann.masterdata.dto.*;
import com.scheidbachmann.masterdata.entity.Sensor;
import com.scheidbachmann.masterdata.repository.SensorRepository;
import com.scheidbachmann.masterdata.service.SensorsService;
import com.scheidbachmann.masterdata.service.impl.ExcelExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/v1/sensors")
@Tag(name = "Sensors")
public class SensorsController {

  private final SensorsService service;

  private final SensorRepository sensorRepository;

  private final ExcelExportService excelExportService;

  public SensorsController(SensorsService service, SensorRepository sensorRepository, ExcelExportService excelExportService) {
    this.service = service;
    this.sensorRepository = sensorRepository;
    this.excelExportService = excelExportService;
  }

  /**
   * Searches Sensors based on specified search parameters and pagination criteria.
   * @param searchParams A map containing the name and the value of the filter
   * @param page
   * @return a response entity containing a page of Sensors
   */

  @Operation(summary = "Search Sensors", description = "Searches all sensors based on specified search parameters and pagination criteria.")
  @PutMapping("/search")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Page<SensorSearchResult>> searchSensors(@RequestBody Map<String, Object> searchParams, Pageable page) throws InterruptedException {
//    Thread.sleep(1000);
    return new ResponseEntity<>(this.service.getSensors(searchParams, page), HttpStatus.OK);
  }

  /**
   * Delete all sensors with the corresponding ids logically by updating field deletedAt to be the current time and date
   * @param ids
   * @return
   */
  @Operation(summary = "Delete Sensors", description = "Delete all sensors with the corresponding ids logically by updating field deletedAt to be the current time and date")
  @PutMapping("/delete")
  @PreAuthorize("hasAuthority('SubAdmin')")

  public ResponseEntity<Void> deleteSensors(@RequestBody List<String>ids) throws InterruptedException {
    Thread.sleep(1000);
    this.service.deleteSensors(ids);
    return new ResponseEntity<>( HttpStatus.OK);
  }

  /**
   * Add a sensor
   * @param sensorDto
   * @return Response entity containing the added sensor
   */

  @Operation(summary = "Add Sensor", description = "Adds a new sensor.")
  @PostMapping()
  @PreAuthorize("hasAuthority('SubAdmin')")
  public ResponseEntity<SensorDto> add(@RequestBody SensorDto sensorDto) throws InterruptedException {
    Thread.sleep(1000);
    return new ResponseEntity<>(service.add(sensorDto), HttpStatus.CREATED);
  }

  /**
   * Update a sensor
   * @param sensorDto
   * @return Response entity containing the updated sensor
   */

  @Operation(summary = "Update Sensor", description = "Updates an existing sensor.")
  @PutMapping()
  @PreAuthorize("hasAuthority('SubAdmin')")

  public ResponseEntity<?> saveSensor(
    @Valid @RequestBody SensorDto sensorDto , BindingResult result) {
    if (result.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
      System.out.print(errors);
      return ResponseEntity.badRequest().body(errors);
    }

    return new ResponseEntity<>(service.updateSensor(sensorDto), HttpStatus.OK);
  }

  @Operation(summary = "Get Sensor by ID", description = "Retrieves a sensor by its ID.")
  @GetMapping("{id}")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<SensorDto> getSensorById(@PathVariable String id) throws InterruptedException {
    Thread.sleep(1000);
    SensorDto sensorDto = service.getSensorById(id);
    if (sensorDto==null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    else return new ResponseEntity<>(sensorDto,HttpStatus.OK);
  }


  @Operation(summary = "checkUniqueSensor", description = "checkUniqueSensor")
@GetMapping("/checkUniqueSensor")
@PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
public ResponseEntity<Map<String, String>> checkUnique(
          @RequestParam(value = "sensorId", required = false) String sensorId,
          @RequestParam("serialNumber") String serialNumber,
          @RequestParam("laneNumber") String laneNumber) {

  String message = service.checkUnique(sensorId, serialNumber, laneNumber);
  Map<String, String> response = new HashMap<>();
  response.put("message", message);

  if (message.equals("Sensor is unique.")) {
    return ResponseEntity.ok(response);
  } else {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }
}

@PostMapping("/sendConnectionPointCreatedToTrxMgr")
@PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
public ResponseEntity<Void> sendConnectionPointCreatedToTrxMgr(@RequestBody ConnectionPointToTrxMgr connectionPointToTrxMgr)  {
  this.service.sendConnectionPointCreatedToTrxMgr(connectionPointToTrxMgr);
  System.out.print("////////////"+connectionPointToTrxMgr);
  return new ResponseEntity<>( HttpStatus.OK);
}


  @PutMapping("/sendConnectionPointChangedToTrxMgr")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Void> sendConnectionPointChangedToTrxMgr(@RequestBody ConnectionPointToTrxMgr connectionPointToTrxMgr)  {
    this.service.sendConnectionPointChangedToTrxMgr(connectionPointToTrxMgr);
    return new ResponseEntity<>( HttpStatus.OK);
  }


  @Operation(summary = "Check if Sensor api key is enabled", description = "Check if Sensor api key is enabled")
  @GetMapping("/mode")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Boolean> getMode() {
  return new ResponseEntity<>(this.service.getWithSensorApiKey(),HttpStatus.OK);
  }


  @Operation(summary = "generateApiKey", description = "generateApiKey")
  @GetMapping("/apiKey")
  @PreAuthorize("hasAuthority('SubAdmin')")
  public ResponseEntity<String> generateApiKey() {
    String apiKey = service.generateApiKey();
    return new ResponseEntity<>(apiKey, HttpStatus.OK);
  }



  @PostMapping("/download")
  public ResponseEntity<byte[]> downloadExcel(@RequestBody Set<Sensor> sensors) {
    try {
      byte[] excelData = excelExportService.generateSensorExcel(sensors);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sensors.xlsx");
      return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @PutMapping("/searchSensors")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Page<SensorExport>> search(@RequestBody Map<String, Object> searchParams, Pageable page) throws InterruptedException {
    return new ResponseEntity<>(this.service.getSensorList(searchParams, page), HttpStatus.OK);
  }





}
