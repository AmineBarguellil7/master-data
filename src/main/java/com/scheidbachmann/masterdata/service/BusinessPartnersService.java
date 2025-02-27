/**
 * Created By Amine Barguellil
 */


package com.scheidbachmann.masterdata.service;

import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.dto.BusinessPartnerExport;
import com.scheidbachmann.masterdata.dto.BusinessPartnerNameId;
import com.scheidbachmann.masterdata.dto.BusinessPartnerSearchResult;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface BusinessPartnersService {

  /**
   * Add a business partner
   * @param businessPartnerDto
   * @return a dto representing the added business partner
   */
  BusinessPartnerDto addBusinessPartner(BusinessPartnerDto businessPartnerDto) throws InterruptedException;

  /**
   * Update a business partner
   * @param businessPartnerDto
   * @return a dto representing the updated business partner
   */
  BusinessPartnerDto updateBusinessPartner(BusinessPartnerDto businessPartnerDto) throws InterruptedException;

  /**
   * Search business partners according to filter with pagination
   * @param searchParams
   * search params can be :
   * tenant
   * providerId
   * filterTerm (name or partner number)
   * ids
   * @param page
   * @return a page containing the result business partners
   */

  Page<BusinessPartnerSearchResult> getBusinessPartners(Map<String, Object> searchParams, Pageable page);

  BusinessPartnerDto getBusinessPartnerById(String id);


  Set<BusinessPartnerNameId> getBusinessPartnersNames();


  Set<BusinessPartnerNameId> getBusinessPartnersNamesByCPType(String type);

  Set<String> getTenants();

    void deleteBusinessPartners(List<String> ids);


  Integer getProviderIdUnderHundred();

  Integer getProviderIdAboveHundred();


  Page<BusinessPartnerExport> getBusinessPartnersList(Map<String, Object> searchParams, Pageable page);

}
