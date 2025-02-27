/**
 * Created By Amine Barguellil
 * Date : 5/8/2024
 * Time : 11:38 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scheidbachmann.masterdata.IntegrationTest;
import com.scheidbachmann.masterdata.dto.ConnectionPointToTrxMgr;
import com.scheidbachmann.masterdata.dto.SensorDto;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.entity.Sensor;
import com.scheidbachmann.masterdata.enums.BusinessPartnerType;
import com.scheidbachmann.masterdata.enums.CarparkTypeEnum;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import com.scheidbachmann.masterdata.enums.DirectionEnum;
import com.scheidbachmann.masterdata.repository.BusinessPartnerRepository;
import com.scheidbachmann.masterdata.repository.ConnectionPointRepository;
import com.scheidbachmann.masterdata.repository.SensorRepository;
import com.scheidbachmann.masterdata.service.SensorsService;
import com.scheidbachmann.masterdata.service.impl.ExcelExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@ContextConfiguration(classes = {SensorsController.class})
public class SensorControllerIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private SensorsService service;


    @MockBean
    private SensorRepository sensorRepository;

    @MockBean
    private BusinessPartnerRepository businessPartnerRepository;

    @MockBean
    private ConnectionPointRepository connectionPointRepository;

    @MockBean
    private ExcelExportService excelExportService;



    @Test
    public void testSearchSensorsShouldPass() throws Exception {


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


        when(businessPartnerRepository.save(businessPartner)).thenReturn(businessPartner);

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


        when(connectionPointRepository.save(cp1)).thenReturn(cp1);
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


        when(sensorRepository.save(sensor1)).thenReturn(sensor1);

        sensorRepository.save(sensor1);
        Sensor sensor2 = new Sensor("temp",
                0L,
                "1234",
                "4",
                DirectionEnum.IN,
                "789",
                "sensor2",
                "tenant2",
                "Test",

                cp1.getId(),
                cp1,null);



        when(sensorRepository.save(sensor2)).thenReturn(sensor2);

        sensorRepository.save(sensor2);
        Sensor sensor3 = new Sensor("temp",
                0L,
                "12345",
                "5",
                DirectionEnum.IN,
                "789",
                "sensor3",
                "tenant3",
                "Test",

                cp1.getId(),
                cp1,null);


        when(sensorRepository.save(sensor3)).thenReturn(sensor3);


        sensorRepository.save(sensor3);




        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/sensors/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tenant\": \"tenant1\"}")
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteSensorsShouldPass() throws Exception {


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


        when(businessPartnerRepository.save(businessPartner)).thenReturn(businessPartner);

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


        when(connectionPointRepository.save(cp1)).thenReturn(cp1);
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


        when(sensorRepository.save(sensor1)).thenReturn(sensor1);

        sensorRepository.save(sensor1);
        Sensor sensor2 = new Sensor("temp",
                0L,
                "1234",
                "4",
                DirectionEnum.IN,
                "789",
                "sensor2",
                "tenant2",
                "Test",

                cp1.getId(),
                cp1,null);



        when(sensorRepository.save(sensor2)).thenReturn(sensor2);

        sensorRepository.save(sensor2);
        Sensor sensor3 = new Sensor("temp",
                0L,
                "12345",
                "5",
                DirectionEnum.IN,
                "789",
                "sensor3",
                "tenant3",
                "Test",

                cp1.getId(),
                cp1,null);


        when(sensorRepository.save(sensor3)).thenReturn(sensor3);


        sensorRepository.save(sensor3);

        List<String> ids = Arrays.asList(sensor1.getId(), sensor3.getId() );

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/sensors/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddSensorShouldPass() throws Exception {



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


        when(businessPartnerRepository.save(businessPartner)).thenReturn(businessPartner);

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

        when(connectionPointRepository.save(cp1)).thenReturn(cp1);

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


        when(sensorRepository.save(sensor1)).thenReturn(sensor1);

        sensorRepository.save(sensor1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sensor1))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isCreated());
    }

    @Test
    public void testSaveSensorShouldPass() throws Exception {
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


        when(businessPartnerRepository.save(businessPartner)).thenReturn(businessPartner);

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

        when(connectionPointRepository.save(cp1)).thenReturn(cp1);

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


        when(sensorRepository.save(sensor1)).thenReturn(sensor1);

        sensorRepository.save(sensor1);


        sensor1.setLaneNumber("2");

        sensorRepository.save(sensor1);



        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sensor1))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSensorByIdShouldPass() throws Exception {
        String sensorId = "123";
        SensorDto sensorDto = new SensorDto();
        sensorDto.setId(sensorId);
        when(service.getSensorById(sensorId)).thenReturn(sensorDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/sensors/{id}", sensorId)
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckUniqueSensorShouldPass() throws Exception {

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


        when(businessPartnerRepository.save(businessPartner)).thenReturn(businessPartner);

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


        when(connectionPointRepository.save(cp1)).thenReturn(cp1);
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


        when(sensorRepository.save(sensor1)).thenReturn(sensor1);

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



        when(sensorRepository.save(sensor2)).thenReturn(sensor2);

        sensorRepository.save(sensor2);
        Sensor sensor3 = new Sensor("temp",
                0L,
                "12345",
                "5",
                DirectionEnum.IN,
                "789",
                "sensor3",
                "tenant1",
                "Test",

                cp1.getId(),
                cp1,null);


        when(sensorRepository.save(sensor3)).thenReturn(sensor3);


        sensorRepository.save(sensor3);


        sensor1.setLaneNumber("6");

        sensorRepository.save(sensor1);


        String message = service.checkUnique(sensor1.getId(), sensor1.getSerialNumber(), sensor1.getLaneNumber());


        when(service.checkUnique(any(), any(), any())).thenReturn(message == null ? "Sensor is unique." : message);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/sensors/checkUniqueSensor")
                        .param("sensorId", sensor1.getId())
                        .param("serialNumber", sensor1.getSerialNumber())
                        .param("laneNumber", sensor1.getLaneNumber())
                        .param("directionEnum", sensor1.getDirection().name())
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Sensor is unique."));



    }

    @Test
    public void testSendConnectionPointCreatedToTrxMgrShouldPass() throws Exception {
        ConnectionPointToTrxMgr connectionPointToTrxMgr = new ConnectionPointToTrxMgr();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/sensors/sendConnectionPointCreatedToTrxMgr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(connectionPointToTrxMgr))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testSendConnectionPointChangedToTrxMgrShouldPass() throws Exception {
        ConnectionPointToTrxMgr connectionPointToTrxMgr = new ConnectionPointToTrxMgr();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/sensors/sendConnectionPointChangedToTrxMgr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(connectionPointToTrxMgr))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());
    }

    // Utility method to convert objects to JSON string
    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }




}
