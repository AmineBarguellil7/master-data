/**
 * Created By Amine Barguellil
 * Date : 5/22/2024
 * Time : 1:13 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.kafka;

import com.scheidbachmann.masterdata.UnitTest;
import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.enums.BusinessPartnerType;
import com.scheidbachmann.masterdata.enums.KafkaTopics;
import com.scheidbachmann.masterdata.kafka.config.BusinessPartner.BusinessPartnerSchema;
import com.scheidbachmann.masterdata.kafka.config.EventMessage;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.kafka.impl.KafkaProducerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;


@UnitTest
@ContextConfiguration(classes = {KafkaProducerImpl.class})
@ExtendWith(OutputCaptureExtension.class)
public class KafkaProducerImplTest<T> {


    @Autowired
    private KafkaProducer kafkaProducer;

    @MockBean
    private KafkaTemplate<String, EventMessage> kafkaTemplate;

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerImpl.class);



    @Test
    void testProduceMessage() {

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

        BusinessPartnerSchema businessPartnerSchema=new BusinessPartnerSchema();

        BeanUtils.copyProperties(businessPartnerDto,businessPartnerSchema);

        KafkaSchema<BusinessPartnerSchema> schema = KafkaSchema.with(businessPartnerSchema, businessPartnerDto.getTenantId(), businessPartner.getClass().getSimpleName()+"Created");

        EventMessage eventMessage =
                EventMessage.of(KafkaSchema.with(schema.getPayload(), businessPartner.getTenantId(),schema.getEventType()));

        kafkaProducer.produceMessage(KafkaTopics.BUSINESS_PARTNER_TOPIC,eventMessage);
    }


}
