package com.scheidbachmann.masterdata.service;

import com.scheidbachmann.masterdata.UnitTest;
import com.scheidbachmann.masterdata.dto.ConnectionPointToTrxMgr;
import com.scheidbachmann.masterdata.dto.SensorDto;
import com.scheidbachmann.masterdata.dto.SensorSearchResult;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.entity.Sensor;
import com.scheidbachmann.masterdata.enums.*;
import com.scheidbachmann.masterdata.kafka.config.ConnectionPointToTrxMgr.ConnectionPointToTrxMgrSchema;
import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.kafka.config.Sensor.SensorSchema;
import com.scheidbachmann.masterdata.mapper.ConnectionPointMapper;
import com.scheidbachmann.masterdata.mapper.SensorMapper;
import com.scheidbachmann.masterdata.repository.BusinessPartnerRepository;
import com.scheidbachmann.masterdata.repository.ConnectionPointRepository;
import com.scheidbachmann.masterdata.repository.SensorRepository;
import com.scheidbachmann.masterdata.repository.impl.SensorRepositoryImpl;
import com.scheidbachmann.masterdata.service.impl.SensorsServiceImpl;
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
import static org.mockito.Mockito.when;

/**
 * @author KaouechHaythem
 */
@ContextConfiguration(classes = {SensorsServiceImpl.class, SensorRepositoryImpl.class})
@ComponentScan(basePackageClasses = {SensorMapper.class, ConnectionPointMapper.class})
@WebAppConfiguration
@UnitTest
public class SensorsServiceImplTest {

  @Autowired
  private SensorRepository sensorRepository;

  @Autowired
  private BusinessPartnerRepository businessPartnerRepository;

  @Autowired
  private ConnectionPointRepository connectionPointRepository;

  @Autowired
  private SensorsService sensorsService;

  @Autowired
  private ConnectionPointMapper connectionPointMapper;

  @Autowired
  private SensorMapper sensorMapper;


    @MockBean
    private EventListenerComponent<KafkaSchema<SensorSchema>> eventListenerComponent;

    @MockBean
    private EventListenerComponent<KafkaSchema<ConnectionPointToTrxMgrSchema>> eventListenerComponent1;

  /**
   * Create Test Instances
   */
  @BeforeEach
  public void saveData() {
    BusinessPartner businessPartner = new BusinessPartner("temp",
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



    businessPartner=businessPartnerRepository.save(businessPartner);

    // Create an empty set of Sensor objects
    Set<Sensor> sensors = new HashSet<>();
    ConnectionPoint cp1 = new ConnectionPoint("temp",
      0L,
      ConnectionPointTypeEnum.FACILITY,
      "cp1",
      "123",
      "123",
      "123",
      "123",
      CarparkTypeEnum.MULTI_STOREY_CARPARK,
      null,
      "123",
      "techPlace",
      null,
      "other",
      false,
      "tenant1",
      "geo1",
      false,
      businessPartner.getId(),
      null,
      businessPartner,
      null,
      sensors,
      null,
      null,null
    );


    cp1=connectionPointRepository.save(cp1);
    Sensor sensor1 = new Sensor("temp",
      0L,
      "123",
      "456",
      DirectionEnum.OUT,
      "789",
      "sensor1",
      "tenant1",
            "Test",

            cp1.getId(),
      cp1,null);



    sensorRepository.save(sensor1);
    Sensor sensor2 = new Sensor("temp",
      0L,
      "1234",
      "456",
      DirectionEnum.IN,
      "789",
      "sensor2",
      "tenant1",
      "Test",
      cp1.getId(),
      cp1,null);


    sensorRepository.save(sensor2);
    Sensor sensor3 = new Sensor("temp",
      0L,
      "12345",
      "456",
      DirectionEnum.IN,
      "789",
      "sensor3",
      "tenant1",
            "Test",
      cp1.getId(),
      cp1,null);



    sensorRepository.save(sensor3);

  }

  /**
   * Test  if search method works  as expected with the filters
   */
  @Test
  public void testSearchParamsShouldPass() {
    Map<String, Object> params = new HashMap<>();
    String cpId = connectionPointRepository.findAll().getFirst().getId();
    params.put(PARAMETER_TENANT, "tenant1");
    params.put(PARAMETER_CONNECTIONP_ID, cpId);
    params.put(PARAMETER_LOCATION_ID, "789");
    if (sensorRepository.findAll().size() >= 2) {
      String s1Id = sensorRepository.findAll().getFirst().getId();
      String s2Id = sensorRepository.findAll().get(1).getId();
      params.put(PARAMETER_IDS, Arrays.asList(s1Id, s2Id));
    }
    Pageable pageable = PageRequest.of(0, 10); // Page number starts from 0
    Page<SensorDto> resultPage = sensorsService.getSensorsList(params, pageable);
    // Get the content of the Page
    List<SensorDto> sensorDtos = resultPage.getContent();
    assertEquals(2, sensorDtos.size());
    // Assert that the fields of each SensorDto equals the expected values
    for (SensorDto sensorDto : sensorDtos) {
      assertEquals("tenant1", sensorDto.getTenantName());
      assertEquals(connectionPointRepository.findAll().getFirst().getId(), sensorDto.getConnectionPointId());
      assertEquals("789", sensorDto.getLocationId());
    }
    //verify tenant filter works
    params = new HashMap<>();
    params.put(PARAMETER_TENANT, "notExpectedToWork");
    resultPage = sensorsService.getSensorsList(params, pageable);
    assertEquals(3, resultPage.getContent().size());

    //verify Connection Point ID filter works
    params = new HashMap<>();
    params.put(PARAMETER_CONNECTIONP_ID, "notExpectedToWork");
    resultPage = sensorsService.getSensorsList(params, pageable);
    assertEquals(0, resultPage.getContent().size());

    //verify Serial Number filter works
    params = new HashMap<>();
    params.put(PARAMETER_SERIAL_NUMBER, "notExpectedToWork");
    resultPage = sensorsService.getSensorsList(params, pageable);
    assertEquals(0, resultPage.getContent().size());

    params = new HashMap<>();
    params.put(PARAMETER_SERIAL_NUMBER, "123");
    resultPage = sensorsService.getSensorsList(params, pageable);
    assertEquals(1, resultPage.getContent().size());

    //verify Location ID filter works
    params = new HashMap<>();
    params.put(PARAMETER_LOCATION_ID, "notExpectedToWork");
    resultPage = sensorsService.getSensorsList(params, pageable);
    assertEquals(0, resultPage.getContent().size());
  }

  @Test
  public void testAddSensorShouldPass() {

    ConnectionPoint cp1 = connectionPointRepository.findAll().getFirst();
    SensorDto sensorDto = new SensorDto("temp",
      0L,
      "123456",
      "456",
      DirectionEnum.IN,
      "789",
      "AddedSensor",
      "tenant1",
            "Test",

            cp1.getId(),
      connectionPointMapper.toDto(cp1));
    // Invoke the add method
    SensorDto addedSensorDto = sensorsService.add(sensorDto);

    // Verify that the addedSensorDto is not null
    assertNotNull(addedSensorDto);
    //test if the filed device name is correct to verify fields are added as expected
    assertEquals("AddedSensor", addedSensorDto.getDeviceName());
    //verify the element was actually added to the database
    assertEquals(3, sensorRepository.findAll().size());

    sensorRepository.deleteById(addedSensorDto.getId());

  }

  /**
   * Test Update Sensor method
   */
  @Test
  public void testUpdateSensorShouldPass() {

    Sensor sensor = sensorRepository.findAll().getFirst();
    sensor.setLaneNumber("updated");
    sensor.setRevision(sensor.getRevision()+1);
    sensorsService.updateSensor(sensorMapper.toDto(sensor));
    Sensor updatedSensor = sensorRepository.findById(sensor.getId()).orElse(null);
    assertNotNull(updatedSensor);
    assertEquals("updated", updatedSensor.getLaneNumber());
  }



  @Test
  public void testDeleteSensorShouldPass() {

    List<Sensor> sensors =sensorRepository.findAll();

    for (Sensor sensor: sensors) {
      sensorRepository.deleteById(sensor.getId());
    }

    Set<Sensor> sensorList=new HashSet<>();

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

    ConnectionPoint cp1 = new ConnectionPoint("temp",
            0L,
            ConnectionPointTypeEnum.FACILITY,
            "cp1",
            "123",
            "123",
            "123",
            "123",
            CarparkTypeEnum.MULTI_STOREY_CARPARK,
            null,
            "123",
            "techPlace",
            null,
            "other",
            false,
            "tenant1",
            "geo1",
            false,
            bp1.getId(),
            null,
            bp1,
            null,
            sensorList,
            null,
            null,null
    );


    cp1=connectionPointRepository.save(cp1);
    Sensor sensor1 = new Sensor("temp",
            0L,
            "123",
            "456",
            DirectionEnum.IN,
            "789",
            "sensor1",
            "tenant1",
            "Test",

            cp1.getId(),
            cp1,null);



    sensorRepository.save(sensor1);


    Sensor sensor2 = new Sensor("temp",
            0L,
            "1234",
            "456",
            DirectionEnum.IN,
            "789",
            "sensor2",
            "tenant1",
            "Test",

            cp1.getId(),
            cp1,null);


    sensorRepository.save(sensor2);
    Sensor sensor3 = new Sensor("temp",
            0L,
            "12345",
            "456",
            DirectionEnum.IN,
            "789",
            "sensor3",
            "tenant1",
            "Test",

            cp1.getId(),
            cp1,null);



    sensorRepository.save(sensor3);

    List<String> ids = Arrays.asList(sensor1.getId(),sensor2.getId());

    sensorsService.deleteSensors(ids);

    List<Sensor> remainingSensors = sensorRepository.findAll()
            .stream()
            .filter(sensor -> sensor.getDeletedAt() == null)
            .toList();
    assertEquals(1, remainingSensors.size());
  }


  @Test
  public void testGetSensors() {
    Map<String, Object> params = new HashMap<>();
    String cpId = connectionPointRepository.findAll().getFirst().getId();
    params.put(PARAMETER_TENANT, "tenant1");
    params.put(PARAMETER_CONNECTIONP_ID, cpId);
    params.put(PARAMETER_LOCATION_ID, "789");
    if (sensorRepository.findAll().size() >= 2) {
      String s1Id = sensorRepository.findAll().getFirst().getId();
      String s2Id = sensorRepository.findAll().get(1).getId();
      params.put(PARAMETER_IDS, Arrays.asList(s1Id, s2Id));
    }
    Pageable pageable = PageRequest.of(0, 10); // Page number starts from 0
    Page<SensorSearchResult> resultPage = sensorsService.getSensors(params, pageable);
    // Get the content of the Page
    List<SensorSearchResult> sensorSearchResults = resultPage.getContent();
    assertEquals(2, sensorSearchResults.size());
    // Assert that the fields of each SensorDto equals the expected values
    for (SensorSearchResult sensorSearchResult : sensorSearchResults) {
      assertEquals("789", sensorSearchResult.getLocationId());
      assertEquals("456", sensorSearchResult.getLaneNumber());
    }
    //verify tenant filter works
    params = new HashMap<>();
    params.put(PARAMETER_TENANT, "notExpectedToWork");
    resultPage = sensorsService.getSensors(params, pageable);
    assertEquals(3, resultPage.getContent().size());

    //verify Connection Point ID filter works
    params = new HashMap<>();
    params.put(PARAMETER_CONNECTIONP_ID, "notExpectedToWork");
    resultPage = sensorsService.getSensors(params, pageable);
    assertEquals(0, resultPage.getContent().size());

    //verify Serial Number filter works
    params = new HashMap<>();
    params.put(PARAMETER_SERIAL_NUMBER, "notExpectedToWork");
    resultPage = sensorsService.getSensors(params, pageable);
    assertEquals(0, resultPage.getContent().size());

    params = new HashMap<>();
    params.put(PARAMETER_SERIAL_NUMBER, "123");
    resultPage = sensorsService.getSensors(params, pageable);
    assertEquals(1, resultPage.getContent().size());

    //verify Location ID filter works
    params = new HashMap<>();
    params.put(PARAMETER_LOCATION_ID, "notExpectedToWork");
    resultPage = sensorsService.getSensors(params, pageable);
    assertEquals(0, resultPage.getContent().size());
  }


  @Test
  public void testSendConnectionPointCreatedToTrxMgr() {
    ConnectionPointToTrxMgr connectionPointToTrxMgr = new ConnectionPointToTrxMgr();
    sensorsService.sendConnectionPointCreatedToTrxMgr(connectionPointToTrxMgr);
  }

  @Test
  public void testSendConnectionPointChangedToTrxMgr() {
      ConnectionPointToTrxMgr connectionPointToTrxMgr = new ConnectionPointToTrxMgr();
      sensorsService.sendConnectionPointChangedToTrxMgr(connectionPointToTrxMgr);
  }

  @Test
  public void testGetSensorByIdSouldPass() {
    Sensor sensor = sensorRepository.findAll().getFirst();
    SensorDto sensorDto = sensorsService.getSensorById(sensor.getId());
    assertEquals(sensor.getId(),sensorDto.getId());
  }

  @Test
  public void testCheckUnique() {

    List<Sensor> sensors =sensorRepository.findAll();

    for (Sensor sensor: sensors) {
      sensorRepository.deleteById(sensor.getId());
    }

    BusinessPartner businessPartner = new BusinessPartner("temp",
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



    businessPartner=businessPartnerRepository.save(businessPartner);

    // Create an empty set of Sensor objects
    Set<Sensor> sensorHashSet = new HashSet<>();
    ConnectionPoint cp1 = new ConnectionPoint("temp",
            0L,
            ConnectionPointTypeEnum.FACILITY,
            "cp1",
            "123",
            "123",
            "123",
            "123",
            CarparkTypeEnum.MULTI_STOREY_CARPARK,
            null,
            "123",
            "techPlace",
            null,
            "other",
            false,
            "tenant1",
            "geo1",
            false,
            businessPartner.getId(),
            null,
            businessPartner,
            null,
            sensorHashSet,
            null,
            null,null
    );


    cp1=connectionPointRepository.save(cp1);
    Sensor sensor1 = new Sensor("temp",
            0L,
            "123",
            "4",
            DirectionEnum.OUT,
            "789",
            "sensor1",
            "tenant1",
            "Test",

            cp1.getId(),
            cp1,null);



    sensorRepository.save(sensor1);
    Sensor sensor2 = new Sensor("temp",
            0L,
            "1234",
            "4",
            DirectionEnum.IN,
            "789",
            "sensor2",
            "tenant1",
            "Test",

            cp1.getId(),
            cp1,null);


    sensorRepository.save(sensor2);
    Sensor sensor3 = new Sensor("temp",
            0L,
            "123",
            "5",
            DirectionEnum.IN,
            "789",
            "sensor3",
            "tenant1",
            "Test",

            cp1.getId(),
            cp1,null);



    sensorRepository.save(sensor3);



    String result = sensorsService.checkUnique(sensor1.getId(), sensor1.getSerialNumber(), sensor1.getLaneNumber());

    assertEquals("The sensor with serial number '123' already exists!", result);
  }



  @Test
  public void testGetSerialNumbers() {

    List<String> serialNumbers = sensorsService.getSerialNumbers();


    assertEquals(3, serialNumbers.size());
    assertEquals("123", serialNumbers.get(0));
    assertEquals("1234", serialNumbers.get(1));
  }









}
