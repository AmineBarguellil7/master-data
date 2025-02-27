package com.scheidbachmann.masterdata.service;

import com.scheidbachmann.masterdata.UnitTest;
import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.dto.BusinessPartnerNameId;
import com.scheidbachmann.masterdata.dto.BusinessPartnerSearchResult;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.enums.BusinessPartnerType;
import com.scheidbachmann.masterdata.kafka.config.BusinessPartner.BusinessPartnerSchema;
import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.mapper.BusinessPartnerMapper;
import com.scheidbachmann.masterdata.repository.BusinessPartnerRepository;
import com.scheidbachmann.masterdata.repository.impl.BusinessPartnersRepositoryImpl;
import com.scheidbachmann.masterdata.service.impl.BusinessPartnersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.*;
import static com.scheidbachmann.masterdata.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author KaouechHaythem
 */
@ContextConfiguration(classes = {BusinessPartnersServiceImpl.class, BusinessPartnersRepositoryImpl.class})
@ComponentScan(basePackageClasses = {BusinessPartnerMapper.class})
@WebAppConfiguration
@UnitTest
public class BusinessPartnersServiceImplTest {
    @Autowired
    private BusinessPartnerRepository businessPartnerRepository;

    @Autowired
    private BusinessPartnersService businessPartnersService;

    @Autowired
    private BusinessPartnerMapper businessPartnerMapper;

    @MockBean
    private ConnectionPointsService connectionPointsService;

    @MockBean
    private ContractsService contractsService;

    @MockBean
    private EventListenerComponent<KafkaSchema<BusinessPartnerSchema>> eventListenerComponent;



    /**
   * Create Test Instances
   */
  @BeforeEach
  public void saveData() {
    BusinessPartner bp1 = new BusinessPartner("temp",
      "123",
      1,
      "567",
      "bp1",
      "12",
      "Ras Jebel",
      "TN",
      false,
      BusinessPartnerType.B2C,
      0L,
      null,
      null,
      null,
      null,
      null,null);
    businessPartnerRepository.save(bp1);
    BusinessPartner bp2 = new BusinessPartner("temp",
      "123",
      1,
      "567",
      "bp2",
      "12",
      "Ras Jebel",
      "TN",
      false,
      BusinessPartnerType.B2C,
      0L,
      null,
      null,
      null,
      null,
      null,null);
    businessPartnerRepository.save(bp2);

  }

  /**
   * Test if search method works with filter params as expected
   */
  @Test
  public void testSearchBusinessPartnersShouldPass() {
    Map<String, Object> params = new HashMap<>();
    params.put(PARAMETER_TENANT, "567");
    params.put(PARAMETER_PROVIDER_ID, 1);
    params.put(PARAMETER_FILTER_TERM, "bp");
    if (businessPartnerRepository.findAll().size() >= 2) {
      String bp1Id = businessPartnerRepository.findAll().getFirst().getId();
      String bp2Id = businessPartnerRepository.findAll().get(1).getId();
      params.put(PARAMETER_IDS, Arrays.asList(bp1Id, bp2Id));
    }
    Pageable pageable = PageRequest.of(0, 10); // Page number starts from 0
    Page<BusinessPartnerSearchResult> resultPage = businessPartnersService.getBusinessPartners(params, pageable);
    // Get the content of the Page
    List<BusinessPartnerSearchResult> businessPartnerDtos = resultPage.getContent();
    assertEquals(2, businessPartnerDtos.size());

    //verify tenant filter works
    params = new HashMap<>();
    params.put(PARAMETER_TENANT, "notExpectedToWork");
    resultPage = businessPartnersService.getBusinessPartners(params, pageable);
    assertEquals(0, resultPage.getContent().size());

    //verify providerID filter works
    params = new HashMap<>();
    params.put(PARAMETER_PROVIDER_ID, 0);
    resultPage = businessPartnersService.getBusinessPartners(params, pageable);
    assertEquals(0, resultPage.getContent().size());

    //verify tenant filter works
    params = new HashMap<>();
    params.put(PARAMETER_FILTER_TERM, "notExpectedToWork");
    resultPage = businessPartnersService.getBusinessPartners(params, pageable);
    assertEquals(0, resultPage.getContent().size());

  }

  /**
   * Test if add method works as expected
   */
  @Test
  public void testAddBusinessPartnerShouldPass() throws InterruptedException {

    BusinessPartnerDto bpDto = new BusinessPartnerDto("temp",
      "123",
      1,
      "567",
      "AddedBP",
      "12",
      "Ras Jebel",
      "TN",
      false,
      BusinessPartnerType.B2C,
      0L,
      null,
      null,
      null,
      null

    );

//    when ( eventListenerComponent.sendToTopic(KafkaTopics.BUSINESS_PARTNER_TOPIC,bpDto,EventType.BusinessPartnerCreated););

    // Invoke the add method
    BusinessPartnerDto addedBusinessPartnerDto = businessPartnersService.addBusinessPartner (bpDto);

    // Verify that the addedBusinessPartnerDto is not null
    assertNotNull(bpDto);
    //test if the filed  name is correct to verify fields are added as expected
    assertEquals("AddedBP", bpDto.getName());
    //verify the element was actually added to the database
    assertEquals(3, businessPartnerRepository.findAll().size());
  }

  /**
   * Test Update Business Partner method
   */
  @Test
  public void testUpdateBusinessPartnerShouldPass() throws InterruptedException {

      BusinessPartner businessPartner = businessPartnerRepository.findAll().getFirst();
    businessPartner.setName("updated");
    businessPartner.setRevision(businessPartner.getRevision() + 1);

    businessPartnersService.updateBusinessPartner(businessPartnerMapper.toDto(businessPartner));
    BusinessPartner updatedBP = businessPartnerRepository.findById(businessPartner.getId()).orElse(null);
    // verify name field is updated
    assertNotNull(updatedBP);
    assertEquals("updated", updatedBP.getName());
  }

  @Test
  public void testGetBusinessPartnerByIdSouldPass() {
    BusinessPartner businessPartner = businessPartnerRepository.findAll().getFirst();
    BusinessPartnerDto businessPartnerDto = businessPartnersService.getBusinessPartnerById(businessPartner.getId());
    assertEquals(businessPartnerDto.getId(), businessPartner.getId());
  }

  @Test
  public void testGetBusinessPartnersNames() {
    Set<BusinessPartnerNameId> names = businessPartnersService.getBusinessPartnersNames();
    assertEquals(2, names.size());
  }

  @Test
  public void TestGetTenantsShouldPass() {
    Set<String> tenants = businessPartnersService.getTenants();
    assertEquals(1, tenants.size());
  }


    @Test
    public void testDeleteBusinessPartnersShouldPass() {

        List<BusinessPartner> businessPartners=businessPartnerRepository.findAll();

        for (BusinessPartner businessPartner: businessPartners) {
            businessPartnerRepository.deleteById(businessPartner.getId());
        }

        BusinessPartner bp1 = new BusinessPartner("temp",
                "123",
                1,
                "567",
                "bp1",
                "12",
                "Ras Jebel",
                "TN",
                false,
                BusinessPartnerType.B2C,
                0L,
                null,
                null,
                null,
                null,
                null,null);
        businessPartnerRepository.save(bp1);
        BusinessPartner bp2 = new BusinessPartner("temp",
                "123",
                1,
                "567",
                "bp2",
                "12",
                "Ras Jebel",
                "TN",
                false,
                BusinessPartnerType.B2C,
                0L,
                null,
                null,
                null,
                null,
                null,null);
        businessPartnerRepository.save(bp2);


        BusinessPartner bp3 = new BusinessPartner("temp",
                "123",
                1,
                "567",
                "bp2",
                "12",
                "Ras Jebel",
                "TN",
                false,
                BusinessPartnerType.B2C,
                0L,
                null,
                null,
                null,
                null,
                null,null);
        businessPartnerRepository.save(bp3);

        List<String> ids = Arrays.asList(bp1.getId(),bp3.getId());

        businessPartnersService.deleteBusinessPartners(ids);

        List<BusinessPartner> remainingBusinessPartners = businessPartnerRepository.findAll()
                .stream()
                .filter(businessPartner -> businessPartner.getDeletedAt() == null)
                .toList();
        assertEquals(3, remainingBusinessPartners.size());



    }





}
