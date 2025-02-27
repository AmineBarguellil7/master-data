package com.scheidbachmann.masterdata.kafka;


import com.scheidbachmann.masterdata.UnitTest;
import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.dto.ConnectionPointDto;
import com.scheidbachmann.masterdata.dto.ContractDto;
import com.scheidbachmann.masterdata.dto.SensorDto;
import com.scheidbachmann.masterdata.entity.*;
import com.scheidbachmann.masterdata.enums.*;
import com.scheidbachmann.masterdata.kafka.config.BusinessPartner.BusinessPartnerSchema;
import com.scheidbachmann.masterdata.kafka.config.ConnectionPoint.ConnectionPointSchema;
import com.scheidbachmann.masterdata.kafka.config.ConnectionPointToTrxMgr.ConnectionPointToTrxMgrSchema;
import com.scheidbachmann.masterdata.kafka.config.Contract.ContractSchema;
import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
import com.scheidbachmann.masterdata.kafka.config.EventMessage;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.kafka.config.Sensor.SensorSchema;
import com.scheidbachmann.masterdata.kafka.impl.KafkaProducerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * @author KaouechHaythem
 */
@UnitTest
@ContextConfiguration(classes = {EventListenerComponent.class})
@ExtendWith(OutputCaptureExtension.class)
public class EventListenerComponentTest {


  private static final Logger logger = LoggerFactory.getLogger(EventListenerComponent.class);


  @Autowired
  private EventListenerComponent<KafkaSchema<BusinessPartnerSchema>> eventListenerBusinessPartner;

  @Autowired
  private EventListenerComponent<KafkaSchema<ConnectionPointSchema>> connectionPointKafkaEventListenerComponent;

    @Autowired
    private EventListenerComponent<KafkaSchema<SensorSchema>> sensorKafkaEventListenerComponent;

    @Autowired
    private EventListenerComponent<KafkaSchema<ContractSchema>> contractKafkaEventListenerComponent;

    @Autowired
    private EventListenerComponent<KafkaSchema<ConnectionPointToTrxMgrSchema>> connectionPointToTrxMgrKafkaEventListenerComponent;

  @MockBean
  private KafkaProducerImpl kafkaProducer;


  @MockBean
  private KafkaTemplate<String, EventMessage> kafkaTemplate;


  @Test
  void testSendToBusinessPartnerTopic() {

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


      BusinessPartnerDto businessPartnerDto=new BusinessPartnerDto();
      BeanUtils.copyProperties(businessPartner, businessPartnerDto);



    if (kafkaProducer != null) {
      logger.info("kafkaProducer bean is created successfully.");
    } else {
      logger.error("kafkaProducer bean is null!");
    }

    if (kafkaTemplate != null) {
      logger.info("kafkaTemplate bean is created successfully.");
    } else {
      logger.error("kafkaTemplate bean is null!");
    }

      BusinessPartnerSchema businessPartnerSchema=new BusinessPartnerSchema();

      BeanUtils.copyProperties(businessPartnerDto,businessPartnerSchema);

      KafkaSchema<BusinessPartnerSchema> schema = KafkaSchema.with(businessPartnerSchema, businessPartnerDto.getTenantId(), businessPartner.getClass().getSimpleName()+"Created");
      eventListenerBusinessPartner.sendToTopic(KafkaTopics.BUSINESS_PARTNER_TOPIC, schema);
  }

  @Test
  void testSendToConnectionPointTopic() {

    OutboundConnectivityCredentials outboundConnectivityCredentials=new OutboundConnectivityCredentials();
    outboundConnectivityCredentials.setAuthType(AuthTypeEnum.BASIC_AUTH);
    InboundConnectivityCredentials inboundConnectivityCredentials=new InboundConnectivityCredentials();
    ConnectionPointConnectivity connectionPointConnectivity=createSampleConnectionPointConnectivityEntity(UUID.randomUUID().toString(),0L,null,null,10,"100.110.120.130","https://evopark.com:4711/services/eConnect","application/json; charset=utf-8","application/json; charset=utf-8",outboundConnectivityCredentials,inboundConnectivityCredentials);
    ConnectionPointService connectionPointService= createSampleConnectionPointServiceEntity(UUID.randomUUID().toString(),0L,null,"f0ddd6b9-4789-4393-96e8-a35eb9678c0a","string",null,null, ServiceConsumerRoleEnum.CONSUMER);
    Set<ConnectionPointService> ConnectionPointServiceList=new HashSet<>();
    ConnectionPointServiceList.add(connectionPointService);

    BusinessPartner businessPartner=new BusinessPartner(UUID.randomUUID().toString(),"BP245692",1,"enco","parking experts","DE","Mönchengladbach","EUR",false, BusinessPartnerType.B2C,0L, LocalDateTime.now(),null,null,null,null,null);

    ConnectionPoint connectionPoint = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,businessPartner,businessPartner.getId(), ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03435","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);


    if (kafkaProducer != null) {
      logger.info("kafkaProducer bean is created successfully.");
    } else {
      logger.error("kafkaProducer bean is null!");
    }


      ConnectionPointDto connectionPointDto=new ConnectionPointDto();
      BeanUtils.copyProperties(connectionPoint, connectionPointDto);

      ConnectionPointSchema connectionPointSchema=new ConnectionPointSchema();

      BeanUtils.copyProperties(connectionPointDto,connectionPointSchema);

      KafkaSchema<ConnectionPointSchema> schema = KafkaSchema.with(connectionPointSchema, connectionPointDto.getTenantName(), connectionPoint.getClass().getSimpleName()+"Created");
      connectionPointKafkaEventListenerComponent.sendToTopic(KafkaTopics.CONNECTION_POINT_TOPIC, schema);

  }

  @Test
  void testSendToSensorTopic() {

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

    Sensor sensor = new Sensor("temp",
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



    EventType eventType = EventType.CREATED;


    if (kafkaProducer != null) {
      logger.info("kafkaProducer bean is created successfully.");
    } else {
      logger.error("kafkaProducer bean is null!");
    }




      SensorDto sensorDto=new SensorDto();
      BeanUtils.copyProperties(sensor, sensorDto);

      SensorSchema sensorSchema=new SensorSchema();

      BeanUtils.copyProperties(sensorDto,sensorSchema);

      KafkaSchema<SensorSchema> schema = KafkaSchema.with(sensorSchema, sensorDto.getTenantName(), sensor.getClass().getSimpleName()+"Created");
      sensorKafkaEventListenerComponent.sendToTopic(KafkaTopics.SENSOR_TOPIC, schema);



  }

  @Test
  void testSendToContractTopic() {


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

    Service service = new Service(UUID.randomUUID().toString(), "Entervo", "Connect", null, null);
    Contract contract = new Contract(UUID.randomUUID().toString(), 0L, supplierLicense, consumerLicense, PriorityLevel.P1, SelectionType.ALL, SelectionType.SELECTED_ONLY, LocalDate.now(), LocalDate.now(), "Econnect", service.getId(), consumer.getId(), supplier.getId(), service, consumer, supplier, null, null,null);


    EventType eventType = EventType.CREATED;


    if (kafkaProducer != null) {
      logger.info("kafkaProducer bean is created successfully.");
    } else {
      logger.error("kafkaProducer bean is null!");
    }

      ContractDto contractDto=new ContractDto();
      BeanUtils.copyProperties(contract, contractDto);

      ContractSchema contractSchema=new ContractSchema();

      BeanUtils.copyProperties(contractDto,contractSchema);

      KafkaSchema<ContractSchema> schema = KafkaSchema.with(contractSchema, null, contract.getClass().getSimpleName()+"Created");
      contractKafkaEventListenerComponent.sendToTopic(KafkaTopics.CONTRACT_TOPIC, schema);



  }



  private ConnectionPoint createSampleConnectionPointEntity(String id, Long revision, BusinessPartner businessPartner, String businessPartnerId, ConnectionPointTypeEnum connectionPointTypeEnum, String name, String locationId, String facilityId, String cellId, String operatorId, CarparkTypeEnum carparkTypeEnum, LocalDateTime localDateTime, ConnectionPointConnectivity connectivity, Set<ConnectionPointService> connectionPointServices, String orderNumber, String technicalPlace, LocalDate activatedAt, String other, boolean withLeaveLoop, String tenantName, String geometryPath, boolean keycloakInboundUser, Set<Contract> consumerContracts, Set<Contract> supplierContracts, Set<Sensor> sensors, LocalDateTime deletedAt) {
    ConnectionPoint connectionPoint=new ConnectionPoint(
            id,
            revision,
            connectionPointTypeEnum,
            name,
            locationId,
            facilityId,
            cellId,
            operatorId,
            carparkTypeEnum,
            localDateTime,
            orderNumber,
            technicalPlace,
            activatedAt,
            other,
            withLeaveLoop,
            tenantName,
            geometryPath,
            keycloakInboundUser,
            businessPartnerId,
            connectivity,
            businessPartner,
            connectionPointServices,
            sensors,
            consumerContracts,
            supplierContracts, deletedAt
    );
    return connectionPoint;
  }


  private ConnectionPointConnectivity createSampleConnectionPointConnectivityEntity(String id,Long revision,ConnectionPoint connectionPoint,String connectionPointId,Integer port,String host,String baseUrl,String requestFormat,String responseFormat,OutboundConnectivityCredentials outboundConnectivityCredentials,InboundConnectivityCredentials inboundConnectivityCredentials) {
    ConnectionPointConnectivity connectionPointConnectivity=new ConnectionPointConnectivity(
            id,
            revision,
            port,
            host,
            baseUrl,
            requestFormat,
            responseFormat,
            outboundConnectivityCredentials,
            inboundConnectivityCredentials,
            connectionPointId,
            connectionPoint
    );
    return connectionPointConnectivity;
  }

  private ConnectionPointService createSampleConnectionPointServiceEntity(String id, Long revision, Service service, String serviceId, String serviceName, ConnectionPoint connectionPoint, String connectionPointId, ServiceConsumerRoleEnum serviceConsumerRoleEnum) {
    ConnectionPointService connectionPointService1 =new ConnectionPointService(id,
            revision,
            serviceName,
            serviceConsumerRoleEnum,
            connectionPointId,
            serviceId,
            connectionPoint,
            service);
    return connectionPointService1;
  }








}
