/**
 * Created By Amine Barguellil
 * Date : 3/8/2024
 * Time : 9:48 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.controller;


import com.scheidbachmann.masterdata.IntegrationTest;
import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.dto.BusinessPartnerNameId;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.enums.BusinessPartnerType;
import com.scheidbachmann.masterdata.kafka.impl.KafkaProducerImpl;
import com.scheidbachmann.masterdata.repository.BusinessPartnerRepository;
import com.scheidbachmann.masterdata.service.BusinessPartnersService;
import com.scheidbachmann.masterdata.service.impl.ExcelExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@IntegrationTest
@ContextConfiguration(classes = BusinessPartnersController.class)
public class BusinessPartnerControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BusinessPartnersService businessPartnersService;

    @MockBean
    private BusinessPartnerRepository businessPartnerRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExcelExportService excelExportService;


    @Autowired
    private JwtDecoder jwtDecoder;


    @Test
    public void testSearchBusinessPartners() throws Exception {


        BusinessPartner bp1 = new BusinessPartner("temp",
                "123",
                1,
                "5678",
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



        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/businesspartners/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tenantId\": \"567\"}")
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    void testAddBusinessPartner() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/businesspartners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bpDto))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }


    @Test
    void testUpdateBusinessPartner() throws Exception {

        BusinessPartner bp1 = new BusinessPartner(UUID.randomUUID().toString(),
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

        when(businessPartnerRepository.save(any(BusinessPartner.class))).thenReturn(bp1);

        BusinessPartner businessPartner = businessPartnerRepository.save(bp1);


        businessPartner.setName("UpdatedName");
        businessPartner.setCity("UpdatedCity");


        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/businesspartners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(businessPartner))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }


    @Test
    void testGetBusinessPartnerById() throws Exception {

        when(businessPartnersService.getBusinessPartnerById("testId")).thenReturn(new BusinessPartnerDto());


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/businesspartners/{id}", "testId")
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isOk());

    }


    @Test
    void testGetBusinessPartnersNames() throws Exception {
        Set<BusinessPartnerNameId> mockResult = new HashSet<>();
        when(businessPartnersService.getBusinessPartnersNames()).thenReturn(mockResult);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/businesspartners/names")
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void testGetTenants() throws Exception {
        Set<String> mockResult = new HashSet<>(); // Mock result
        when(businessPartnersService.getTenants()).thenReturn(mockResult);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/businesspartners/tenants")
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }



    @Test
    void testDeleteBusinessPartners() throws Exception {

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


        List<String> ids = Arrays.asList(bpDto.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/businesspartners/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids))
                        .with(jwt().jwt((jwt) -> jwt.subject("amine@gmail.com"))))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }




}
