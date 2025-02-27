/**
 * Created By Amine Barguellil
 */


package com.scheidbachmann.masterdata.controller;

import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.dto.BusinessPartnerExport;
import com.scheidbachmann.masterdata.dto.BusinessPartnerNameId;
import com.scheidbachmann.masterdata.dto.BusinessPartnerSearchResult;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.repository.BusinessPartnerRepository;
import com.scheidbachmann.masterdata.service.BusinessPartnersService;
import com.scheidbachmann.masterdata.service.impl.ExcelExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/v1/businesspartners")
@Tag(name = "BusinessPartners")
public class BusinessPartnersController {

  private final BusinessPartnersService businessPartnersService;

  private final  BusinessPartnerRepository businessPartnerRepository;

  private final ExcelExportService excelExportService;

  public BusinessPartnersController(BusinessPartnersService businessPartnersService, BusinessPartnerRepository businessPartnerRepository, ExcelExportService excelExportService) {
    this.businessPartnersService = businessPartnersService;
    this.businessPartnerRepository = businessPartnerRepository;
    this.excelExportService = excelExportService;
  }


  //exemple de request : http://localhost:8081/businesspartners/v1/search?size=10&page=0&sort=name,DESC

  /**
  * Searches Business partners based on specified search parameters and pagination criteria.
   * @param searchParams A map containing the name and the value of the filter
   * @param page
   * @return a response entity containing a page of business partners
   */
  @Operation(summary = "Search Business Partners", description = "Searches business partners based on specified search parameters and pagination criteria.")
  @PutMapping("/search")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Page<BusinessPartnerSearchResult>> searchBusinessPartners(@RequestBody Map<String, Object> searchParams, Pageable page) throws InterruptedException {
    return new ResponseEntity<>(this.businessPartnersService.getBusinessPartners(searchParams, page), HttpStatus.OK);
  }


  /**
   * Add a business partner a
   * @param businessPartnerDto
   * @return Response entity containing the added business partner
   */
  @Operation(summary = "Add Business Partner", description = "Adds a new business partner.")
  @PostMapping()
  @PreAuthorize("hasAuthority('SubAdmin')")
  public ResponseEntity<BusinessPartnerDto> addBusinessPartner(@RequestBody BusinessPartnerDto businessPartnerDto) throws InterruptedException {
    return new ResponseEntity<>(businessPartnersService.addBusinessPartner(businessPartnerDto), HttpStatus.CREATED);
  }

  /**
   * Update a business partner
   * @param businessPartnerDto
   * @return Response entity containing the Updated business partner
   */
  @Operation(summary = "Update Business Partner", description = "Updates an existing business partner.")
  @PutMapping()
  @PreAuthorize("hasAuthority('SubAdmin')")
  public ResponseEntity<BusinessPartnerDto> updateBusinessPartner(@RequestBody BusinessPartnerDto businessPartnerDto) throws InterruptedException {

    return new ResponseEntity<BusinessPartnerDto>(businessPartnersService.updateBusinessPartner(businessPartnerDto), HttpStatus.OK);
  }

  /**
   * Get a business partner by id
   * @param id
   * @return response entity containing the result business partner or null if it doesn t exist
   */
  @Operation(summary = "Get Business Partner by ID", description = "Retrieves a business partner by its ID.")
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<BusinessPartnerDto> getBusinessPartnerById(@PathVariable String id) throws InterruptedException {

    BusinessPartnerDto partnerDto = businessPartnersService.getBusinessPartnerById(id);
    if (partnerDto==null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    else return new ResponseEntity<>(partnerDto,HttpStatus.OK);
  }



  /**
   * Get all  business partner names (useful in filter)
   * @return List of all business partners names
   */
  @Operation(summary = "Get Business Partner Names", description = "Gets all business partner names.")
  @PutMapping("/names/type")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Set<BusinessPartnerNameId>> getBusinessPartnersNamesByCPType(@RequestBody String type){
    Set<BusinessPartnerNameId> result = businessPartnersService.getBusinessPartnersNamesByCPType(type);
    if(result.isEmpty())return new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
    else return new ResponseEntity<>(result,HttpStatus.OK);
  }
  /**
   * Get all  business partner names (useful in filter)
   * @return List of all business partners names
   */
  @Operation(summary = "Get Business Partner Names", description = "Gets all business partner names.")
  @GetMapping("/names")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Set<BusinessPartnerNameId>> getBusinessPartnersNames(){
    Set<BusinessPartnerNameId> result = businessPartnersService.getBusinessPartnersNames();
    if(result.isEmpty())return new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
    else return new ResponseEntity<>(result,HttpStatus.OK);
  }

  /**
   * Get all Tenants (useful in filter)
   * @return List of all Tenants
   */
  @Operation(summary = "Get Tenants", description = "Gets all tenants.")
  @GetMapping("/tenants")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Set<String>> getTenants(){
    Set<String> result = businessPartnersService.getTenants();
    if(result.isEmpty())return new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
    else return new ResponseEntity<>(result,HttpStatus.OK);
  }


  /**
   * Delete all Business Partners with the corresponding ids logically by updating field deletedAt to be the current time and date
   * @param ids
   * @return
   */
  @Operation(summary = "Delete Business Partners", description = "Delete all Business Partners with the corresponding ids logically by updating field deletedAt to be the current time and date")
  @PutMapping("/delete")
  @PreAuthorize("hasAuthority('SubAdmin')")
  public ResponseEntity<?> deleteBusinessPartners(@RequestBody List<String>ids) throws InterruptedException {
    try {
      businessPartnersService.deleteBusinessPartners(ids);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (IllegalStateException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", ex.getMessage()));
    }
  }



  @PostMapping("/download")
  public ResponseEntity<byte[]> downloadExcel(@RequestBody Set<BusinessPartner> businessPartners) {
    try {
      byte[] excelData = excelExportService.generateBusinessPartnerExcel(businessPartners);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=business-partners.xlsx");
      return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Get the minimum provider id smaller than 100
   * @return integer
   */
  @Operation(summary = "Get the minimum provider id smaller than 100 ", description = "Get the minimum provider id smaller than 100 ")
  @GetMapping("/provider/under")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Integer> getProviderIdUnderHundred()  {
    Integer providerId =this.businessPartnersService.getProviderIdUnderHundred();
    if (providerId!=null){
      return new ResponseEntity<>(providerId,HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

    }

  }
  /**
   * Get the minimum provider id bigger than 100
   * @return integer
   */
  @Operation(summary = "Get the minimum provider id bigger than 100 ", description = "Get the minimum provider id bigger than 100 ")
  @GetMapping("/provider/above")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Integer> getProviderIdAboveHundred()  {
    Integer providerId =this.businessPartnersService.getProviderIdAboveHundred();
    if (providerId!=null){
      return new ResponseEntity<>(providerId,HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

    }


  }





  @PutMapping("/searchBusinessPartners")
  @PreAuthorize("hasAuthority('SubAdmin') OR hasAuthority('SimpleUser')")
  public ResponseEntity<Page<BusinessPartnerExport>> search(@RequestBody Map<String, Object> searchParams, Pageable page) throws InterruptedException {
    return new ResponseEntity<>(this.businessPartnersService.getBusinessPartnersList(searchParams, page), HttpStatus.OK);
  }



}

