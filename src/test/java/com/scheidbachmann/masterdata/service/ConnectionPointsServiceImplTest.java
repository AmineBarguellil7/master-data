

/**
 * @author Amine Barguellil
 */


package com.scheidbachmann.masterdata.service;


import com.scheidbachmann.masterdata.UnitTest;
import com.scheidbachmann.masterdata.dto.*;
import com.scheidbachmann.masterdata.entity.*;
import com.scheidbachmann.masterdata.enums.*;
import com.scheidbachmann.masterdata.kafka.config.ConnectionPoint.ConnectionPointSchema;
import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.mapper.BusinessPartnerMapper;
import com.scheidbachmann.masterdata.mapper.ConnectionPointMapper;
import com.scheidbachmann.masterdata.repository.*;
import com.scheidbachmann.masterdata.repository.impl.ConnectionPointRepositoryImpl;
import com.scheidbachmann.masterdata.service.impl.ConnectionPointsServiceImpl;
import com.scheidbachmann.masterdata.utils.Constants;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.scheidbachmann.masterdata.utils.Constants.PARAMETER_IDS;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;



@ContextConfiguration(classes = {ConnectionPointsServiceImpl.class, ConnectionPointRepositoryImpl.class})
@ComponentScan(basePackageClasses = {ConnectionPointMapper.class})
@WebAppConfiguration
@UnitTest
public class ConnectionPointsServiceImplTest {


    @Mock
    private ConnectionPointRepository connectionPointRepository;


    @Autowired
    private BusinessPartnerRepository businessPartnerRepository;

    @MockBean
    private IamService iamService;

    @Autowired
    private ConnectionPointMapper connectionPointMapper;

    @Autowired
    private ConnectionPointsService service;

    @Autowired
    private BusinessPartnerMapper businessPartnerMapper;


    @MockBean
    private  SensorsService sensorsService;

    @MockBean
    private EventListenerComponent<KafkaSchema<ConnectionPointSchema>> eventListenerComponent;

    @Test
    void testAddConnectionPoint() {


        OutboundConnectivityCredentials outboundConnectivityCredentials=new OutboundConnectivityCredentials();
        InboundConnectivityCredentials inboundConnectivityCredentials=new InboundConnectivityCredentials();

        ConnectionPointConnectivity connectionPointConnectivity=createSampleConnectionPointConnectivityEntity(UUID.randomUUID().toString(),0L,null,null,10,"100.110.120.130","https://evopark.com:4711/services/eConnect","application/json; charset=utf-8","application/json; charset=utf-8",outboundConnectivityCredentials,inboundConnectivityCredentials);
        ConnectionPointService connectionPointService= createSampleConnectionPointServiceEntity(UUID.randomUUID().toString(),0L,null,"047ddf54-d13c-43a8-a5fb-8474adedef60","string",null,null, ServiceConsumerRoleEnum.CONSUMER);
        Set<ConnectionPointService> ConnectionPointServiceList=new HashSet<>();
        ConnectionPointServiceList.add(connectionPointService);

        ConnectionPoint connectionPoint = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,null,null, ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03435","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);

        ConnectionPointDto result =service.add(connectionPointMapper.toDto(connectionPoint));

        assertNotNull(result);

    }


    @Test
    void testGetConnectionPoints() {


        OutboundConnectivityCredentials outboundConnectivityCredentials=new OutboundConnectivityCredentials();
        outboundConnectivityCredentials.setAuthType(AuthTypeEnum.BASIC_AUTH);
        InboundConnectivityCredentials inboundConnectivityCredentials=new InboundConnectivityCredentials();
        ConnectionPointConnectivity connectionPointConnectivity=createSampleConnectionPointConnectivityEntity(UUID.randomUUID().toString(),0L,null,null,10,"100.110.120.130","https://evopark.com:4711/services/eConnect","application/json; charset=utf-8","application/json; charset=utf-8",outboundConnectivityCredentials,inboundConnectivityCredentials);
        ConnectionPointService connectionPointService= createSampleConnectionPointServiceEntity(UUID.randomUUID().toString(),0L,null,"f0ddd6b9-4789-4393-96e8-a35eb9678c0a","string",null,null, ServiceConsumerRoleEnum.CONSUMER);
        Set<ConnectionPointService> ConnectionPointServiceList=new HashSet<>();
        ConnectionPointServiceList.add(connectionPointService);

        BusinessPartner businessPartner=new BusinessPartner(UUID.randomUUID().toString(),"BP245692",1,"enco","parking experts","DE","Mönchengladbach","EUR",false, BusinessPartnerType.B2C,0L,LocalDateTime.now(),null,null,null,null,null);


        BusinessPartner addedBusinessPartner= businessPartnerRepository.save(businessPartner);




        ConnectionPoint connectionPoint = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,addedBusinessPartner,addedBusinessPartner.getId(), ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03435","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);

        ConnectionPointDto addedConnectionPoint  = service.add(connectionPointMapper.toDto(connectionPoint));



//        addedConnectionPoint.setBusinessPartner(businessPartnerMapper.toDto(addedBusinessPartner));

        when(connectionPointRepository.findAll()).thenReturn(Collections.singletonList(connectionPoint));

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put(Constants.PARAMETER_TENANT_NAME, "String");

        searchParams.put(Constants.PARAMETER_GEOMETRY_PATH,"string");

//        searchParams.put(Constants.PARAMETER_BP_ID,addedBusinessPartner.getId());

        searchParams.put(Constants.PARAMETER_NAME,"samir");

        List<String> locationIds = Arrays.asList("SB012.001.03435");

        searchParams.put(Constants.PARAMETER_LOCATION_IDS,locationIds);

        searchParams.put(Constants.PARAMETER_SERVICE_ID,"f0ddd6b9-4789-4393-96e8-a35eb9678c0a");

        searchParams.put(Constants.PARAMETER_CP_ROLE, ServiceConsumerRoleEnum.CONSUMER.name());


        List<String> connectionPointIds = Arrays.asList(addedConnectionPoint.getId());
        searchParams.put(PARAMETER_IDS, connectionPointIds);


        Pageable page = PageRequest.of(0, 20);






        Page<ConnectionPointSearchResult> result = service.getConnectionPoints(searchParams,page);


        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

    }


    @Test
    void testGetConnectionPointById() {


        OutboundConnectivityCredentials outboundConnectivityCredentials=new OutboundConnectivityCredentials();
        InboundConnectivityCredentials inboundConnectivityCredentials=new InboundConnectivityCredentials();

        ConnectionPointConnectivity connectionPointConnectivity=createSampleConnectionPointConnectivityEntity(UUID.randomUUID().toString(),0L,null,null,10,"100.110.120.131","https://evopark.com:4711/services/eConnect","application/json; charset=utf-8","application/json; charset=utf-8",outboundConnectivityCredentials,inboundConnectivityCredentials);
        ConnectionPointService connectionPointService= createSampleConnectionPointServiceEntity(UUID.randomUUID().toString(),0L,null,"047ddf54-d13c-43a8-a5fb-8474adedef60","string",null,null, ServiceConsumerRoleEnum.CONSUMER);
        Set<ConnectionPointService> ConnectionPointServiceList=new HashSet<>();
        ConnectionPointServiceList.add(connectionPointService);

        ConnectionPoint connectionPoint = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,null,null, ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03435","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);

        ConnectionPointDto addedConnectionPointDto=service.add(connectionPointMapper.toDto(connectionPoint));

        ConnectionPointDto result = service.getConnectionPoint(addedConnectionPointDto.getId());

        assertNotNull(result);

        assertEquals(connectionPoint.getName(), result.getName());


    }


    @Test
    void testUpdateConnectionPoint() throws InterruptedException {


        OutboundConnectivityCredentials outboundConnectivityCredentials=new OutboundConnectivityCredentials();
        outboundConnectivityCredentials.setAuthType(AuthTypeEnum.BASIC_AUTH);
        InboundConnectivityCredentials inboundConnectivityCredentials=new InboundConnectivityCredentials();
        ConnectionPointConnectivity connectionPointConnectivity=createSampleConnectionPointConnectivityEntity(UUID.randomUUID().toString(),0L,null,null,10,"100.110.120.130","https://evopark.com:4711/services/eConnect","application/json; charset=utf-8","application/json; charset=utf-8",outboundConnectivityCredentials,inboundConnectivityCredentials);
        ConnectionPointService connectionPointService= createSampleConnectionPointServiceEntity(UUID.randomUUID().toString(),0L,null,"f0ddd6b9-4789-4393-96e8-a35eb9678c0a","string",null,null, ServiceConsumerRoleEnum.CONSUMER);
        Set<ConnectionPointService> ConnectionPointServiceList=new HashSet<>();
        ConnectionPointServiceList.add(connectionPointService);

        BusinessPartner businessPartner=new BusinessPartner(UUID.randomUUID().toString(),"BP245692",1,"enco","parking experts","DE","Mönchengladbach","EUR",false, BusinessPartnerType.B2C,0L,LocalDateTime.now(),null,null,null,null,null);



        BusinessPartner addedBusinessPartner= businessPartnerRepository.save(businessPartner);

        ConnectionPoint connectionPoint = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,addedBusinessPartner,addedBusinessPartner.getId(), ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03435","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);

        ConnectionPointDto addedConnectionPoint  = service.add(connectionPointMapper.toDto(connectionPoint));

        addedConnectionPoint.setName("UpdatedName");
        addedConnectionPoint.setLocationId("SB012.001.03433");


        ConnectionPointDto updatedConnectionPointDto = service.saveConnectionPoint(addedConnectionPoint);


        ConnectionPointDto retrievedConnectionPointDto = service.getConnectionPoint(updatedConnectionPointDto.getId());


        assertNotNull(retrievedConnectionPointDto);
        assertEquals("UpdatedName", retrievedConnectionPointDto.getName());
        assertEquals("SB012.001.03433", retrievedConnectionPointDto.getLocationId());

    }









    private ConnectionPoint createSampleConnectionPointEntity(String id,Long revision,BusinessPartner businessPartner,String businessPartnerId,ConnectionPointTypeEnum connectionPointTypeEnum,String name,String locationId,String facilityId,String cellId,String operatorId,CarparkTypeEnum carparkTypeEnum,LocalDateTime localDateTime,ConnectionPointConnectivity connectivity,Set<ConnectionPointService> connectionPointServices,String orderNumber,String technicalPlace,LocalDate activatedAt,String other,boolean withLeaveLoop,String tenantName,String geometryPath,boolean keycloakInboundUser,Set<Contract> consumerContracts,Set<Contract> supplierContracts,Set<Sensor> sensors,LocalDateTime deletedAt) {
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

    private ConnectionPointService createSampleConnectionPointServiceEntity(String id,Long revision,Service service,String serviceId,String serviceName,ConnectionPoint connectionPoint,String connectionPointId,ServiceConsumerRoleEnum serviceConsumerRoleEnum) {
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


    @Test
    public void testGetConnectionPointsByBPId() {


        OutboundConnectivityCredentials outboundConnectivityCredentials=new OutboundConnectivityCredentials();
        outboundConnectivityCredentials.setAuthType(AuthTypeEnum.BASIC_AUTH);
        InboundConnectivityCredentials inboundConnectivityCredentials=new InboundConnectivityCredentials();
        ConnectionPointConnectivity connectionPointConnectivity=createSampleConnectionPointConnectivityEntity(UUID.randomUUID().toString(),0L,null,null,10,"100.110.120.130","https://evopark.com:4711/services/eConnect","application/json; charset=utf-8","application/json; charset=utf-8",outboundConnectivityCredentials,inboundConnectivityCredentials);
        ConnectionPointService connectionPointService= createSampleConnectionPointServiceEntity(UUID.randomUUID().toString(),0L,null,"f0ddd6b9-4789-4393-96e8-a35eb9678c0a","string",null,null, ServiceConsumerRoleEnum.CONSUMER);
        Set<ConnectionPointService> ConnectionPointServiceList=new HashSet<>();
        ConnectionPointServiceList.add(connectionPointService);

        BusinessPartner businessPartner1=new BusinessPartner(UUID.randomUUID().toString(),"BP245692",1,"enco","parking experts","DE","Mönchengladbach","EUR",false, BusinessPartnerType.B2C,0L,LocalDateTime.now(),null,null,null,null,null);

        BusinessPartner businessPartner2=new BusinessPartner(UUID.randomUUID().toString(),"BP245692",1,"enco","parking experts","DE","Mönchengladbach","EUR",false, BusinessPartnerType.B2C,0L,LocalDateTime.now(),null,null,null,null,null);


        BusinessPartner addedBusinessPartner1= businessPartnerRepository.save(businessPartner1);

        BusinessPartner addedBusinessPartner2= businessPartnerRepository.save(businessPartner2);

        ConnectionPoint connectionPoint1 = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,addedBusinessPartner1,addedBusinessPartner1.getId(), ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03435","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);

        ConnectionPoint connectionPoint2 = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,addedBusinessPartner2,addedBusinessPartner2.getId(), ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03439","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);



        ConnectionPointDto addedConnectionPoint1  = service.add(connectionPointMapper.toDto(connectionPoint1));

        ConnectionPointDto addedConnectionPoint2  = service.add(connectionPointMapper.toDto(connectionPoint2));



//        List<ConnectionPointSearchResult> result = service.getConnectionPointsByBPId(connectionPoint1.getBusinessPartnerId());
//
//
//        assertEquals(addedConnectionPoint1.getId(), result.get(0).getId());
//        assertNotEquals(addedConnectionPoint2.getId(),result.get(0).getId());
    }




    @Test
    public void testDeleteConnectionPointsShouldPass() {


        OutboundConnectivityCredentials outboundConnectivityCredentials=new OutboundConnectivityCredentials();
        outboundConnectivityCredentials.setAuthType(AuthTypeEnum.BASIC_AUTH);
        InboundConnectivityCredentials inboundConnectivityCredentials=new InboundConnectivityCredentials();
        ConnectionPointConnectivity connectionPointConnectivity=createSampleConnectionPointConnectivityEntity(UUID.randomUUID().toString(),0L,null,null,10,"100.110.120.130","https://evopark.com:4711/services/eConnect","application/json; charset=utf-8","application/json; charset=utf-8",outboundConnectivityCredentials,inboundConnectivityCredentials);
        ConnectionPointService connectionPointService= createSampleConnectionPointServiceEntity(UUID.randomUUID().toString(),0L,null,"f0ddd6b9-4789-4393-96e8-a35eb9678c0a","string",null,null, ServiceConsumerRoleEnum.CONSUMER);
        Set<ConnectionPointService> ConnectionPointServiceList=new HashSet<>();
        ConnectionPointServiceList.add(connectionPointService);

        BusinessPartner businessPartner1=new BusinessPartner(UUID.randomUUID().toString(),"BP245692",1,"enco","parking experts","DE","Mönchengladbach","EUR",false, BusinessPartnerType.B2C,0L,LocalDateTime.now(),null,null,null,null,null);

        BusinessPartner businessPartner2=new BusinessPartner(UUID.randomUUID().toString(),"BP245692",1,"enco","parking experts","DE","Mönchengladbach","EUR",false, BusinessPartnerType.B2C,0L,LocalDateTime.now(),null,null,null,null,null);


        BusinessPartner addedBusinessPartner1= businessPartnerRepository.save(businessPartner1);

        BusinessPartner addedBusinessPartner2= businessPartnerRepository.save(businessPartner2);

        ConnectionPoint connectionPoint1 = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,addedBusinessPartner1,addedBusinessPartner1.getId(), ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03435","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);

        ConnectionPoint connectionPoint2 = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,addedBusinessPartner2,addedBusinessPartner2.getId(), ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03439","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);



        ConnectionPointDto addedConnectionPoint1  = service.add(connectionPointMapper.toDto(connectionPoint1));

        ConnectionPointDto addedConnectionPoint2  = service.add(connectionPointMapper.toDto(connectionPoint2));


        List<String> ids = Arrays.asList(addedConnectionPoint1.getId(),addedConnectionPoint2.getId());

        service.deleteConnectionPoints(ids);

        List<ConnectionPoint> remainingConnectionPoints = connectionPointRepository.findAll()
                .stream()
                .filter(connectionPoint -> connectionPoint.getDeletedAt() == null)
                .toList();

        assertEquals(0,remainingConnectionPoints.size());


    }










}
