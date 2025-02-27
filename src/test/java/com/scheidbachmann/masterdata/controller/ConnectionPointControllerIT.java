package com.scheidbachmann.masterdata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scheidbachmann.masterdata.IntegrationTest;
import com.scheidbachmann.masterdata.dto.ConnectionPointDto;
import com.scheidbachmann.masterdata.entity.*;
import com.scheidbachmann.masterdata.enums.*;
import com.scheidbachmann.masterdata.mapper.ConnectionPointMapper;
import com.scheidbachmann.masterdata.repository.BusinessPartnerRepository;
import com.scheidbachmann.masterdata.repository.ConnectionPointRepository;
import com.scheidbachmann.masterdata.service.ConnectionPointsService;
import com.scheidbachmann.masterdata.service.impl.ExcelExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@ContextConfiguration(classes = {ConnectionPointsController.class})
public class ConnectionPointControllerIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConnectionPointsService service;

    @MockBean
    private ExcelExportService excelExportService;

    @MockBean
    private ConnectionPointRepository connectionPointRepository;

    @MockBean
    private ConnectionPointMapper connectionPointMapper;

    @MockBean
    private BusinessPartnerRepository businessPartnerRepository;

    @Test
    public void testSearchConnectionPointsShouldPass() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/connectionpoints/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"filter\": \"test\"}")
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetConnectionPointByIdShouldPass() throws Exception {
        String connectionPointId = "123";
        ConnectionPointDto connectionPointDto = new ConnectionPointDto();
        connectionPointDto.setId(connectionPointId);
        when(service.getConnectionPoint(connectionPointId)).thenReturn(connectionPointDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/connectionpoints/{id}", connectionPointId)
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddConnectionPointShouldPass() throws Exception {
        ConnectionPointDto connectionPointDto = new ConnectionPointDto();
        // Set connection point details
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/connectionpoints/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(connectionPointDto))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateConnectionPointShouldPass() throws Exception {

        OutboundConnectivityCredentials outboundConnectivityCredentials=new OutboundConnectivityCredentials();
        outboundConnectivityCredentials.setAuthType(AuthTypeEnum.BASIC_AUTH);
        InboundConnectivityCredentials inboundConnectivityCredentials=new InboundConnectivityCredentials();
        ConnectionPointConnectivity connectionPointConnectivity=createSampleConnectionPointConnectivityEntity(UUID.randomUUID().toString(),0L,null,null,10,"100.110.120.130","https://evopark.com:4711/services/eConnect","application/json; charset=utf-8","application/json; charset=utf-8",outboundConnectivityCredentials,inboundConnectivityCredentials);
        ConnectionPointService connectionPointService= createSampleConnectionPointServiceEntity(UUID.randomUUID().toString(),0L,null,"f0ddd6b9-4789-4393-96e8-a35eb9678c0a","string",null,null, ServiceConsumerRoleEnum.CONSUMER);
        Set<ConnectionPointService> ConnectionPointServiceList=new HashSet<>();
        ConnectionPointServiceList.add(connectionPointService);

        BusinessPartner businessPartner1=new BusinessPartner(UUID.randomUUID().toString(),"BP245692",1,"enco","parking experts","DE","Mönchengladbach","EUR",false, BusinessPartnerType.B2C,0L, LocalDateTime.now(),null,null,null,null,null);

        when (businessPartnerRepository.save(businessPartner1)).thenReturn(businessPartner1);


        BusinessPartner addedBusinessPartner1= businessPartnerRepository.save(businessPartner1);


        ConnectionPoint connectionPoint1 = createSampleConnectionPointEntity(UUID.randomUUID().toString(),0L,addedBusinessPartner1,addedBusinessPartner1.getId(), ConnectionPointTypeEnum.FACILITY,"samir","SB012.001.03435","string","string","string", CarparkTypeEnum._UNSPECIFIED_, LocalDateTime.now(),connectionPointConnectivity,ConnectionPointServiceList,"string","string", LocalDate.now(),"string",false,"string","string",false,null,null,null,null);

        // Set connection point details
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/connectionpoints/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(connectionPoint1))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetConnectionPointTypesShouldPass() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/connectionpoints/getConnectionPointsTypes")
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetConnectionPointsByBPIdShouldPass() throws Exception {
        String businessPartnerId = "123";
        ConnectionPointTypeEnum connectionPointTypeEnum=ConnectionPointTypeEnum.FACILITY;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/connectionpoints/getConnectionPointsByBPId/{BPid}/{type}", businessPartnerId,connectionPointTypeEnum)
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteConnectionPointsShouldPass() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/connectionpoints/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(Arrays.asList("1", "2", "3")))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
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


}
