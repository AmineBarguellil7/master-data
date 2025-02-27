package com.scheidbachmann.masterdata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scheidbachmann.masterdata.IntegrationTest;
import com.scheidbachmann.masterdata.dto.ContractDto;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.Contract;
import com.scheidbachmann.masterdata.entity.ContractLicense;
import com.scheidbachmann.masterdata.entity.Service;
import com.scheidbachmann.masterdata.enums.BusinessPartnerType;
import com.scheidbachmann.masterdata.enums.LicenseType;
import com.scheidbachmann.masterdata.enums.PriorityLevel;
import com.scheidbachmann.masterdata.enums.SelectionType;
import com.scheidbachmann.masterdata.mapper.ContractMapper;
import com.scheidbachmann.masterdata.repository.BusinessPartnerRepository;
import com.scheidbachmann.masterdata.repository.ContractRepository;
import com.scheidbachmann.masterdata.repository.ServiceRepository;
import com.scheidbachmann.masterdata.service.BusinessPartnersService;
import com.scheidbachmann.masterdata.service.ContractsService;
import com.scheidbachmann.masterdata.service.impl.ExcelExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;


import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@ContextConfiguration(classes = {ContractsController.class})
public class ContractsControllerIT {


    @Autowired
    private ObjectMapper objectMapper;
  @Autowired
  private MockMvc mockMvc;


  @MockBean
  private ContractsService contractsService;


    @MockBean
  private BusinessPartnerRepository businessPartnerRepository;

    @MockBean
  private ServiceRepository serviceRepository;

    @MockBean
    private ExcelExportService excelExportService;

  @MockBean
  private ContractRepository contractRepository;

  @MockBean
  private ContractMapper contractMapper;

  @MockBean
  private BusinessPartnersService businessPartnersService;



  @Test
  public void testSearchContractsShouldPass() throws Exception {


      ContractLicense supplierLicense = new ContractLicense();
      supplierLicense.setLicenseType(LicenseType.PER_MONTH);
      supplierLicense.setPrice(20.0);
      supplierLicense.setCurrency("EUR");
      ContractLicense consumerLicense = new ContractLicense();
      consumerLicense.setLicenseType(LicenseType.PER_MONTH);
      consumerLicense.setPrice(10.0);
      consumerLicense.setCurrency("EUR");
      BusinessPartner consumer = new BusinessPartner(UUID.randomUUID().toString(), "BP245692", 1, "enco", "parking experts", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null,null);
      BusinessPartner supplier = new BusinessPartner(UUID.randomUUID().toString(), "BP245695", 2, "enco1", "parking 5", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null,null);


      when(businessPartnerRepository.save(consumer)).thenReturn(consumer);
      when(businessPartnerRepository.save(supplier)).thenReturn(supplier);

      Service service = new Service(UUID.randomUUID().toString(), "Entervo", "Connect", null, null);

      when(serviceRepository.save(service)).thenReturn(service);


      Contract contract1 = new Contract(UUID.randomUUID().toString(), 0L, supplierLicense, consumerLicense, PriorityLevel.P1, SelectionType.ALL, SelectionType.SELECTED_ONLY, LocalDate.now(), LocalDate.now(), "Econnect", service.getId(), consumer.getId(), supplier.getId(), service, consumer, supplier, null, null,null);


      mockMvc.perform(MockMvcRequestBuilders
                      .put("/v1/contracts/search")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"supplierId\": \"" + contract1.getSupplierId() + "\"}")
                      .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
              .andExpect(status().isOk());

  }

  @Test
  public void testGetContractByIdShouldPass() throws Exception {
    when(contractsService.getContractById("testId")).thenReturn(new ContractDto());


    mockMvc.perform(MockMvcRequestBuilders
                      .get("/v1/contracts/{id}", "testId")
                      .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
              .andExpect(status().isOk());

  }


  @Test
  public void testUpdateContractShouldPass() throws Exception {


      ContractLicense supplierLicense = new ContractLicense();
      supplierLicense.setLicenseType(LicenseType.PER_MONTH);
      supplierLicense.setPrice(20.0);
      supplierLicense.setCurrency("EUR");
      ContractLicense consumerLicense = new ContractLicense();
      consumerLicense.setLicenseType(LicenseType.PER_MONTH);
      consumerLicense.setPrice(10.0);
      consumerLicense.setCurrency("EUR");
      BusinessPartner consumer = new BusinessPartner(UUID.randomUUID().toString(), "BP245692", 1, "enco", "parking experts", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null,null);
      BusinessPartner supplier = new BusinessPartner(UUID.randomUUID().toString(), "BP245695", 2, "enco1", "parking 5", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null,null);


      when(businessPartnerRepository.save(consumer)).thenReturn(consumer);
      when(businessPartnerRepository.save(supplier)).thenReturn(supplier);

      Service service = new Service(UUID.randomUUID().toString(), "Entervo", "Connect", null, null);

      when(serviceRepository.save(service)).thenReturn(service);


      Contract contract1 = new Contract(UUID.randomUUID().toString(), 0L, supplierLicense, consumerLicense, PriorityLevel.P1, SelectionType.ALL, SelectionType.SELECTED_ONLY, LocalDate.now(), LocalDate.now(), "Econnect", service.getId(), consumer.getId(), supplier.getId(), service, consumer, supplier, null, null,null);


      when(contractRepository.save(contract1)).thenReturn(contract1);


      contract1.setServiceName("Hello");

      contractRepository.save(contract1);


      // Perform the PUT request
      mockMvc.perform(MockMvcRequestBuilders
                      .put("/v1/contracts")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(asJsonString(contract1))
                      .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
              .andExpect(status().isOk())
              .andReturn();

  }


    @Test
    void testDeleteContracts() throws Exception {
        ContractLicense supplierLicense = new ContractLicense();
        supplierLicense.setLicenseType(LicenseType.PER_MONTH);
        supplierLicense.setPrice(20.0);
        supplierLicense.setCurrency("EUR");
        ContractLicense consumerLicense = new ContractLicense();
        consumerLicense.setLicenseType(LicenseType.PER_MONTH);
        consumerLicense.setPrice(10.0);
        consumerLicense.setCurrency("EUR");
        BusinessPartner consumer = new BusinessPartner(UUID.randomUUID().toString(), "BP245692", 1, "enco", "parking experts", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null,null);
        BusinessPartner supplier = new BusinessPartner(UUID.randomUUID().toString(), "BP245695", 2, "enco1", "parking 5", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null,null);


        when(businessPartnerRepository.save(consumer)).thenReturn(consumer);
        when(businessPartnerRepository.save(supplier)).thenReturn(supplier);

        Service service = new Service(UUID.randomUUID().toString(), "Entervo", "Connect", null, null);

        when(serviceRepository.save(service)).thenReturn(service);


        Contract contract1 = new Contract(UUID.randomUUID().toString(), 0L, supplierLicense, consumerLicense, PriorityLevel.P1, SelectionType.ALL, SelectionType.SELECTED_ONLY, LocalDate.now(), LocalDate.now(), "Econnect", service.getId(), consumer.getId(), supplier.getId(), service, consumer, supplier, null, null,null);


        when(contractRepository.save(contract1)).thenReturn(contract1);


        List<String> ids = Arrays.asList(contract1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/contracts/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }

    @Test
    public void testGetServicesShouldPass() throws Exception {

      Service service1 = new Service(UUID.randomUUID().toString(), "Service 1", "Description 1", null, null);
      Service service2  = new Service(UUID.randomUUID().toString(), "Service 2", "Description 2", null, null);

        when(serviceRepository.save(service1)).thenReturn(service1);
        when(serviceRepository.save(service2)).thenReturn(service2);

        List<Service> services=Arrays.asList(service1,service2);

        when(contractsService.getServices()).thenReturn(services);

        // Perform the GET request and verify the response
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/contracts/getServices")
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }



    @Test
    public void testAddContractShouldPass() throws Exception {
        ContractLicense supplierLicense = new ContractLicense();
        supplierLicense.setLicenseType(LicenseType.PER_MONTH);
        supplierLicense.setPrice(20.0);
        supplierLicense.setCurrency("EUR");
        ContractLicense consumerLicense = new ContractLicense();
        consumerLicense.setLicenseType(LicenseType.PER_MONTH);
        consumerLicense.setPrice(10.0);
        consumerLicense.setCurrency("EUR");
        BusinessPartner consumer = new BusinessPartner(UUID.randomUUID().toString(), "BP245692", 1, "enco", "parking experts", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null,null);
        BusinessPartner supplier = new BusinessPartner(UUID.randomUUID().toString(), "BP245695", 2, "enco1", "parking 5", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null,null);


        when(businessPartnerRepository.save(consumer)).thenReturn(consumer);
        when(businessPartnerRepository.save(supplier)).thenReturn(supplier);

        Service service = new Service(UUID.randomUUID().toString(), "Entervo", "Connect", null, null);

        when(serviceRepository.save(service)).thenReturn(service);


        Contract contract1 = new Contract(UUID.randomUUID().toString(), 0L, supplierLicense, consumerLicense, PriorityLevel.P1, SelectionType.ALL, SelectionType.SELECTED_ONLY, LocalDate.now(), LocalDate.now(), "Econnect", service.getId(), consumer.getId(), supplier.getId(), service, consumer, supplier, null, null,null);


        // Perform the POST request and verify the response
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/contracts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(contract1))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isCreated());
    }











    // Utility method to convert objects to JSON string
  private String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    return objectMapper.writeValueAsString(obj);
  }
}






