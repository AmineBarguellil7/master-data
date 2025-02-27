/**
 * @author Amine Barguellil
 */


package com.scheidbachmann.masterdata.service;

import com.scheidbachmann.masterdata.UnitTest;
import com.scheidbachmann.masterdata.dto.ContractDto;
import com.scheidbachmann.masterdata.dto.ContractSearchResult;
import com.scheidbachmann.masterdata.entity.*;
import com.scheidbachmann.masterdata.enums.*;
import com.scheidbachmann.masterdata.kafka.config.Contract.ContractSchema;
import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.mapper.ContractMapper;
import com.scheidbachmann.masterdata.repository.BusinessPartnerRepository;
import com.scheidbachmann.masterdata.repository.ContractRepository;
import com.scheidbachmann.masterdata.repository.ServiceRepository;
import com.scheidbachmann.masterdata.repository.impl.ContractsRepositoryImpl;
import com.scheidbachmann.masterdata.service.impl.ContractsServiceImpl;
import com.scheidbachmann.masterdata.utils.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ContextConfiguration(classes = {ContractsServiceImpl.class, ContractsRepositoryImpl.class})
@ComponentScan(basePackageClasses = {ContractMapper.class})
@WebAppConfiguration
@UnitTest
public class ContractsServiceImplTest {


  @Autowired
  private ContractRepository contractRepository;

  @Autowired
  private BusinessPartnerRepository businessPartnerRepository;

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private ContractMapper contractMapper;

  @Autowired
  private ContractsService contractsService;

  @MockBean
  private ConnectionPointsService connectionPointsService;


    @MockBean
    private EventListenerComponent<KafkaSchema<ContractSchema>> eventListenerComponent;


  @Test
  void testGetContracts() {

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


    BusinessPartner consumer1 = businessPartnerRepository.save(consumer);
    BusinessPartner supplier1 = businessPartnerRepository.save(supplier);
    Service service = new Service(UUID.randomUUID().toString(), "Entervo", "Connect", null, null);
    Service addedService = serviceRepository.save(service);
    Contract contract1 = new Contract(UUID.randomUUID().toString(), 0L, supplierLicense, consumerLicense, PriorityLevel.P1, SelectionType.ALL, SelectionType.SELECTED_ONLY, LocalDate.now(), LocalDate.now(), "Econnect", addedService.getId(), consumer1.getId(), supplier1.getId(), addedService, consumer1, supplier1, null, null,null);



    contractRepository.save(contract1);

    Map<String, Object> searchParams = new HashMap<>();

    searchParams.put(Constants.PARAMETER_SUPPLIER_ID, supplier1.getId());

    searchParams.put(Constants.PARAMETER_CONSUMER_ID, consumer1.getId());

    Pageable page = PageRequest.of(0, 20);


    Page<ContractSearchResult> result = contractsService.getContracts(searchParams, page);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());

//    searchParams.put(Constants.PARAMETER_CONSUMER_ID, consumer1.getId());

//    result = contractsService.getContracts(searchParams, page);
//
//
//    assertNotNull(result);
//    assertEquals(1, result.getTotalElements());


    searchParams.put(Constants.PARAMETER_SERVICE_ID, addedService.getId());


    result = contractsService.getContracts(searchParams, page);


    assertNotNull(result);
    assertEquals(1, result.getTotalElements());


    searchParams.put(Constants.PARAMETER_SUPPLIER_ID, "f0ddd6b9-4789-4393-96e8-a35eb9678c0a");


    result = contractsService.getContracts(searchParams, page);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());

    searchParams.put(Constants.PARAMETER_CONSUMER_ID, "f0ddd6b9-4789-4393-96e8-a35eb9678c0a");


    result = contractsService.getContracts(searchParams, page);

    assertNotNull(result);
    assertEquals(0, result.getTotalElements());

    searchParams.put(Constants.PARAMETER_SERVICE_ID, "f0ddd6b9-4789-4393-96e8-a35eb9678c0a");


    result = contractsService.getContracts(searchParams, page);

    assertNotNull(result);
    assertEquals(0, result.getTotalElements());
  }


  @Test
  void testGetContractById() {


    ContractLicense supplierLicense = new ContractLicense();
    supplierLicense.setLicenseType(LicenseType.PER_MONTH);
    supplierLicense.setPrice(20.0);
    supplierLicense.setCurrency("EUR");
    ContractLicense consumerLicense = new ContractLicense();
    consumerLicense.setLicenseType(LicenseType.PER_MONTH);
    consumerLicense.setPrice(10.0);
    consumerLicense.setCurrency("EUR");
//    BusinessPartner consumer = new BusinessPartner(UUID.randomUUID().toString(), "BP245692", 1, "enco", "parking experts", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null);
//    BusinessPartner supplier = new BusinessPartner(UUID.randomUUID().toString(), "BP245695", 2, "enco1", "parking 5", "DE", "Mönchengladbach", "EUR", false, BusinessPartnerType.B2C, 0L, LocalDateTime.now(), null, null, null, null);



      BusinessPartner consumer=new BusinessPartner();
      consumer.setPartnerNumber("123");
      consumer.setName("samir");
      consumer.setCity("Tunis");

      BusinessPartner supplier=new BusinessPartner();
      supplier.setPartnerNumber("12345");
      supplier.setName("lol");
      supplier.setCity("Tunis");


      BusinessPartner consumer1 = businessPartnerRepository.save(consumer);
    BusinessPartner supplier1 = businessPartnerRepository.save(supplier);
    Service service = new Service(UUID.randomUUID().toString(), "Entervo", "Connect", null, null);
    Service addedService = serviceRepository.save(service);
//    Contract contract1 = new Contract(UUID.randomUUID().toString(), 0L, supplierLicense, consumerLicense, PriorityLevel.P1, SelectionType.ALL, SelectionType.SELECTED_ONLY, LocalDate.now(), LocalDate.now(), "Econnect", addedService.getId(), consumer1.getId(), supplier1.getId(), addedService, consumer1, supplier1, null, null);

      Contract contract1=new Contract();
      contract1.setServiceName("Online Auth");

    Contract addedContract= contractRepository.save(contract1);


    ContractDto result = contractsService.getContractById(addedContract.getId());

    assertNotNull(result);

    assertEquals(addedContract.getContractEnd(), result.getContractEnd());

  }


  @Test
  void testDeleteContract() {


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


    BusinessPartner consumer1 = businessPartnerRepository.save(consumer);
    BusinessPartner supplier1 = businessPartnerRepository.save(supplier);
    Service service = new Service(UUID.randomUUID().toString(), "Entervo", "Connect", null, null);
    Service addedService = serviceRepository.save(service);
    Contract contract1 = new Contract(UUID.randomUUID().toString(), 0L, supplierLicense, consumerLicense, PriorityLevel.P1, SelectionType.ALL, SelectionType.SELECTED_ONLY, LocalDate.now(), LocalDate.now(), "Econnect", addedService.getId(), consumer1.getId(), supplier1.getId(), addedService, consumer1, supplier1, null, null,null);
    Contract contract2 = new Contract(UUID.randomUUID().toString(), 0L, supplierLicense, consumerLicense, PriorityLevel.P1, SelectionType.ALL, SelectionType.SELECTED_ONLY, LocalDate.now(), LocalDate.now(), "Econnect", addedService.getId(), consumer1.getId(), supplier1.getId(), addedService, consumer1, supplier1, null, null,null);


    Contract addedContract1= contractRepository.save(contract1);
    Contract addedContract2= contractRepository.save(contract2);


    List<String> ids = Arrays.asList(addedContract1.getId(),addedContract2.getId());

    contractsService.deleteContracts(ids);

    List<Contract> remainingContracts = contractRepository.findAll()
            .stream()
            .filter(contract -> contract.getDeletedAt() == null)
            .toList();

    assertEquals(0,remainingContracts.size());

  }


  @Test
  void testUpdateContract() {

    ContractLicense supplierLicense = new ContractLicense();
    supplierLicense.setLicenseType(LicenseType.PER_MONTH);
    supplierLicense.setPrice(20.0);
    supplierLicense.setCurrency("EUR");
    ContractLicense consumerLicense = new ContractLicense();
    consumerLicense.setLicenseType(LicenseType.PER_MONTH);
    consumerLicense.setPrice(10.0);
    consumerLicense.setCurrency("EUR");



      BusinessPartner consumer=new BusinessPartner();
      consumer.setPartnerNumber("123");
      consumer.setName("samir");
      consumer.setCity("Tunis");

      BusinessPartner supplier=new BusinessPartner();
      supplier.setPartnerNumber("12345");
      supplier.setName("lol");
      supplier.setCity("Tunis");

      Contract contract1=new Contract();
      contract1.setServiceName("Online Auth");

    Contract addedContract= contractRepository.save(contract1);


    Service service1 = new Service(UUID.randomUUID().toString(), "Entervo", "Co", null, null);
    addedContract.setPriorityLevel(PriorityLevel.P1);
    addedContract.setService(service1);


    Contract contract = contractRepository.save(addedContract);


    ContractDto retrievedContract = contractsService.getContractById(contract.getId());


    assertNotNull(retrievedContract);
    assertEquals(PriorityLevel.P1, retrievedContract.getPriorityLevel());

  }


  @Test
  public void testGetServices() {

    List<Service> result = contractsService.getServices();


    assertNotEquals(0, result.size());
  }


  @Test
  void testAddContract() {


    ContractLicense supplierLicense = new ContractLicense();
    supplierLicense.setLicenseType(LicenseType.PER_MONTH);
    supplierLicense.setPrice(20.0);
    supplierLicense.setCurrency("EUR");
    ContractLicense consumerLicense = new ContractLicense();
    consumerLicense.setLicenseType(LicenseType.PER_MONTH);
    consumerLicense.setPrice(10.0);
    consumerLicense.setCurrency("EUR");



    BusinessPartner consumer=new BusinessPartner();
    consumer.setPartnerNumber("123");
    consumer.setName("samir");
    consumer.setCity("Tunis");

    BusinessPartner supplier=new BusinessPartner();
    supplier.setPartnerNumber("12345");
    supplier.setName("lol");
    supplier.setCity("Tunis");

    Contract contract1=new Contract();
    contract1.setServiceName("Online Auth");


    Service service1 = new Service(UUID.randomUUID().toString(), "Entervo", "Co", null, null);
    contract1.setPriorityLevel(PriorityLevel.P1);
    contract1.setService(service1);

    ContractDto contractDto=contractMapper.toDto(contract1);


    ContractDto contractDto1 = contractsService.add(contractDto);

    // Assert
    assertNotNull(contractDto1);
    assertEquals(contractDto.getServiceName(), contractDto1.getServiceName());
    assertEquals(contractDto.getPriorityLevel(), contractDto1.getPriorityLevel());
  }



}
