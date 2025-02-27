/**
 * Created By Amine Barguellil
 * Date : 2/21/2024
 * Time : 10:35 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.controller;

import com.scheidbachmann.masterdata.dto.BusinessPartnerExport;
import com.scheidbachmann.masterdata.dto.ConnectionPointDto;
import com.scheidbachmann.masterdata.dto.ConnectionPointExport;
import com.scheidbachmann.masterdata.dto.ConnectionPointSearchResult;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import com.scheidbachmann.masterdata.exceptions.ResourceNotFoundException;
import com.scheidbachmann.masterdata.repository.ConnectionPointRepository;
import com.scheidbachmann.masterdata.service.ConnectionPointsService;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/v1/connectionpoints")
@Tag(name = "ConnectionPoints")
public class ConnectionPointsController {

  private final ConnectionPointsService connectionPointsService;

  private final ConnectionPointRepository connectionPointRepository;

  private final ExcelExportService excelExportService;

  public ConnectionPointsController(ConnectionPointsService connectionPointsService, ConnectionPointRepository connectionPointRepository, ExcelExportService excelExportService) {
    this.connectionPointsService = connectionPointsService;
    this.connectionPointRepository = connectionPointRepository;
    this.excelExportService = excelExportService;
  }

  /**
   * PUT /search : Searches connectionPoints based on specified search parameters and pagination criteria.
   *
   * @param searchParams A map representing the search parameters to filter connectionPoints.
   * @param page         A Pageable object specifying the page information for the result set.
   * @return A ResponseEntity containing a Page of ConnectionPointDto objects matching the search criteria.
   */

  @Operation(summary = "Search Connection Points", description = "Searches connection points based on specified search parameters and pagination criteria.")
  @PutMapping("/search")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Page<ConnectionPointSearchResult>> searchConnectionPoints(@RequestBody Map<String, Object> searchParams, Pageable page) throws InterruptedException {
    return new ResponseEntity<>(this.connectionPointsService.getConnectionPoints(searchParams, page), HttpStatus.OK);
  }


  /**
   * Retrieves a specific connection point based on the provided identifier.
   *
   * @param id The unique identifier of the connection point to be retrieved.
   * @return A ResponseEntity containing the ConnectionPointDto object corresponding to the provided id.
   * Returns HttpStatus.OK if the operation is successful.
   * Throws ResourceNotFoundException if the connection point with the given id is not found.
   */

  @Operation(summary = "Get Connection Point by ID", description = "Retrieves a specific connection point based on the provided identifier.")
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<ConnectionPointDto> getConnectionPoint(@PathVariable String id) throws InterruptedException {

    ConnectionPointDto connectionPointDto = this.connectionPointsService.getConnectionPoint(id);
    if (connectionPointDto == null) {
      throw ResourceNotFoundException.with(id);
    } else {
      return new ResponseEntity<>(connectionPointDto, HttpStatus.OK);
    }
  }

  /**
   * Adds a new connection point.
   *
   * @param connectionPointDto The ConnectionPointDto object representing the details of the new connection point.
   * @return A ResponseEntity containing the ConnectionPointDto object representing the added connection point.
   * Returns HttpStatus.CREATED if the operation is successful.
   */
  @Operation(summary = "Add Connection Point", description = "Adds a new connection point.")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('SubAdmin')")
  public ResponseEntity<ConnectionPointDto> addConnectionPoint(@RequestBody ConnectionPointDto connectionPointDto) {
    return new ResponseEntity<>(connectionPointsService.add(connectionPointDto), HttpStatus.CREATED);
  }

  /**
   * Updates an existing connection point.
   *
   * @param connectionPoint The ConnectionPointDto object representing the updated details of the connection point.
   *                        It must be a valid instance, validated by the specified validation annotations.
   * @return A ResponseEntity containing the ConnectionPointDto object representing the updated connection point.
   * Returns HttpStatus.OK if the operation is successful.
   */
  @Operation(summary = "Update Connection Point", description = "Updates an existing connection point.")
  @PutMapping("/update")
  @PreAuthorize("hasAuthority('SubAdmin')")
  public ResponseEntity<ConnectionPointDto> saveConnectionPoint(
    @Valid @RequestBody ConnectionPointDto connectionPoint
  ) throws InterruptedException {

//    System.out.print("/////////////////" +connectionPoint);
//    System.out.print("/////////////////"+ connectionPointsService.saveConnectionPoint(connectionPoint)+"\n");
    return new ResponseEntity<>(connectionPointsService.saveConnectionPoint(connectionPoint), HttpStatus.OK);
  }

//  @PutMapping("/getBusinessPartners")
//  public ResponseEntity<List<Map<String, String>>> getBusinessPartners(@RequestBody String name) {
//    List<Map<String, String>> result = connectionPointsService.testConnectionPointsByBusinessPartnerName(name);
//    return new ResponseEntity<>(result, HttpStatus.OK);
//  }


  @Operation(summary = "Get Connection Point Types", description = "Retrieves the list of connection point types.")
  @PutMapping("/getConnectionPointsTypes")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public List<ConnectionPointTypeEnum> getConnectionPointsTypes() {
    return Arrays.asList(ConnectionPointTypeEnum.values());
  }


  @Operation(summary = "Get Connection Points by Business Partner ID", description = "Retrieves the list of connection points associated with a business partner ID.")
  @GetMapping("/getConnectionPointsByBPId/{BPid}/{type}")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public List<ConnectionPointSearchResult> getConnectionPointsByBPId(@PathVariable String BPid ,@PathVariable String type ) throws InterruptedException {
    return connectionPointsService.getConnectionPointsByBPIdAndType(BPid,type);
  }

  /**
   * Delete all Connection Points with the corresponding ids logically by updating field deletedAt to be the current time and date
   * @param ids
   * @return
   */
  @Operation(summary = "Delete Connection Points", description = "Delete all Connection Points with the corresponding ids logically by updating field deletedAt to be the current time and date")
  @PutMapping("/delete")
  @PreAuthorize("hasAuthority('SubAdmin')")
  public ResponseEntity<?> deleteConnectionPoints(@RequestBody List<String>ids) throws InterruptedException {
    try {
      this.connectionPointsService.deleteConnectionPoints(ids);
      return new ResponseEntity<>(HttpStatus.OK);
    }
    catch (IllegalStateException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", ex.getMessage()));
    }
  }

  @PostMapping("/checkLocation")
  public ResponseEntity<Boolean> checkUniqueLocationId(@RequestPart(required = false)  String id , @RequestPart String locationId) {
    boolean isUnique = connectionPointsService.checkUniqueLocationId(id,locationId);
    return ResponseEntity.ok(isUnique);
  }

  @PostMapping("/download")
  public ResponseEntity<byte[]> downloadExcel(@RequestBody Set<ConnectionPoint> connectionPoints) {
    try {
      byte[] excelData = excelExportService.generateConnectionPointExcel(connectionPoints);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=connection-points.xlsx");
      return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @PutMapping("/searchConnectionPoints")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Page<ConnectionPointExport>> search(@RequestBody Map<String, Object> searchParams, Pageable page) throws InterruptedException {
    return new ResponseEntity<>(this.connectionPointsService.getConnectionPointsList(searchParams, page), HttpStatus.OK);
  }




}
