/**
 * Created By Amine Barguellil
 * Date : 2/21/2024
 * Time : 11:04 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.controller;


import com.scheidbachmann.masterdata.dto.ConnectionPointExport;
import com.scheidbachmann.masterdata.dto.ContractDto;
import com.scheidbachmann.masterdata.dto.ContractExport;
import com.scheidbachmann.masterdata.dto.ContractSearchResult;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.entity.Contract;
import com.scheidbachmann.masterdata.entity.Service;
import com.scheidbachmann.masterdata.enums.LicenseType;
import com.scheidbachmann.masterdata.repository.ContractRepository;
import com.scheidbachmann.masterdata.service.ContractsService;
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
import java.util.*;

@RestController
@RequestMapping("/v1/contracts")
@Tag(name = "Contracts")
public class ContractsController {

  private final ContractsService contractsService;


  private final ContractRepository contractRepository;

  private final ExcelExportService excelExportService;

  public ContractsController(ContractsService contractsService, ContractRepository contractRepository, ExcelExportService excelExportService) {
    this.contractsService = contractsService;
    this.contractRepository = contractRepository;
    this.excelExportService = excelExportService;
  }

  /**
   * PUT /search : Searches contracts based on specified search parameters and pagination criteria.
   * @param searchParams A map representing the search parameters to filter contracts.
   * @param page         A Pageable object specifying the page information for the result set.
   * @return A ResponseEntity containing a Page of ContractDto objects matching the search criteria.
   */

  @Operation(summary = "Search Contracts", description = "Searches contracts based on specified search parameters and pagination criteria.")
  @PutMapping("/search")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Page<ContractSearchResult>> searchContracts(@RequestBody Map<String, Object> searchParams, Pageable page) throws InterruptedException {

    return new ResponseEntity<>(this.contractsService.getContracts(searchParams, page), HttpStatus.OK);
  }


  @Operation(summary = "Update Contract", description = "Updates an existing contract.")
  @PutMapping()
  @PreAuthorize("hasAuthority('SubAdmin')")

  public ResponseEntity<?> updateContract(@Valid @RequestBody ContractDto contractDto ,  BindingResult result){
    if (result.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
      System.out.print(errors);
      return ResponseEntity.badRequest().body(errors);
    }

    return new ResponseEntity<>(contractsService.updateContract(contractDto),HttpStatus.OK);
  }
  @Operation(summary = "Get Contract by ID", description = "Retrieves a contract by its ID.")
  @GetMapping("{id}")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<ContractDto> getContractById(@PathVariable String id) throws InterruptedException {

    ContractDto contractDto = contractsService.getContractById(id);
    if (contractDto==null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    else return new ResponseEntity<>(contractDto,HttpStatus.OK);
  }

 /* *//**
   * POST /add : saves a new contract
   * @return returns the saved contract
   */
  @PostMapping("/add")
  public ResponseEntity<ContractDto> addContract(@RequestBody ContractDto contractDto) throws InterruptedException {

    return new ResponseEntity<>(contractsService.add(contractDto), HttpStatus.CREATED);
  }


  @GetMapping("/getServices")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<List<Service>> getServices() throws InterruptedException {

    return new  ResponseEntity<>(contractsService.getServices(),HttpStatus.OK);
  }

  @GetMapping("/getAllLicenseTypes")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<List<LicenseType>> getAllLicenseTypes() {
    return new  ResponseEntity<>(contractsService.getAllLicenseTypes(),HttpStatus.OK);
  }

  /**
   * Delete all Contracts with the corresponding ids logically by updating field deletedAt to be the current time and date
   * @param ids
   * @return
   */
  @Operation(summary = "Delete Contracts", description = "Delete all Contracts with the corresponding ids logically by updating field deletedAt to be the current time and date")
  @PutMapping("/delete")
  @PreAuthorize("hasAuthority('SubAdmin')")
  public ResponseEntity<?> deleteContracts(@RequestBody List<String>ids) throws InterruptedException {

    try {
      this.contractsService.deleteContracts(ids);
      return new ResponseEntity<>( HttpStatus.OK);
    }
    catch (IllegalStateException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", ex.getMessage()));
    }

  }



  @PostMapping("/download")
  public ResponseEntity<byte[]> downloadExcel(@RequestBody Set<Contract> contracts) {
    try {
      byte[] excelData = excelExportService.generateContractExcel(contracts);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contracts.xlsx");
      return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @PutMapping("/searchContracts")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Page<ContractExport>> search(@RequestBody Map<String, Object> searchParams, Pageable page) throws InterruptedException {
    return new ResponseEntity<>(this.contractsService.getContractsList(searchParams, page), HttpStatus.OK);
  }


}
