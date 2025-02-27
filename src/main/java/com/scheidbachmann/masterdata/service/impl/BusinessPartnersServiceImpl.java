
package com.scheidbachmann.masterdata.service.impl;

import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.dto.BusinessPartnerExport;
import com.scheidbachmann.masterdata.dto.BusinessPartnerNameId;
import com.scheidbachmann.masterdata.dto.BusinessPartnerSearchResult;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.Contract;
import com.scheidbachmann.masterdata.entity.Person;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import com.scheidbachmann.masterdata.enums.EventType;
import com.scheidbachmann.masterdata.enums.KafkaTopics;
import com.scheidbachmann.masterdata.kafka.config.BusinessPartner.BusinessPartnerSchema;
import com.scheidbachmann.masterdata.kafka.config.EventListenerComponent;
import com.scheidbachmann.masterdata.kafka.config.KafkaSchema;
import com.scheidbachmann.masterdata.mapper.BusinessPartnerMapper;
import com.scheidbachmann.masterdata.mapper.PersonMapper;
import com.scheidbachmann.masterdata.repository.BusinessPartnerRepository;
import com.scheidbachmann.masterdata.repository.ConnectionPointRepository;
import com.scheidbachmann.masterdata.repository.ContractRepository;
import com.scheidbachmann.masterdata.repository.PersonRepository;
import com.scheidbachmann.masterdata.repository.impl.BusinessPartnersRepositoryImpl;
import com.scheidbachmann.masterdata.service.BusinessPartnersService;
import com.scheidbachmann.masterdata.service.ConnectionPointsService;
import com.scheidbachmann.masterdata.service.ContractsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Slf4j
public class BusinessPartnersServiceImpl implements BusinessPartnersService {


  private final BusinessPartnerRepository businessPartnerRepository;
  private final BusinessPartnerMapper businessPartnerMapper;
  private final PersonMapper personMapper;
  private final PersonRepository personRepository;

  private final ContractRepository contractRepository;
  public static final int DEFAULT_PAGE_SIZE = 20000;
  public static final int MAX_PAGE_SIZE = 20000;
  private final BusinessPartnersRepositoryImpl repositoryQuery;

  private final ConnectionPointsService connectionPointService;

  private final ConnectionPointRepository connectionPointRepository;


  private final ContractsService contractsService;


//  private final EventListenerComponent<BusinessPartnerKafka> eventListenerComponent;

  private final EventListenerComponent<KafkaSchema<BusinessPartnerSchema>> eventListenerComponent;

  public BusinessPartnersServiceImpl(BusinessPartnerRepository businessPartnerRepository,
                                     PersonMapper personMapper,
                                     BusinessPartnerMapper businessPartnerMapper,
                                     PersonRepository personRepository,
                                     ContractRepository contractRepository,
                                     BusinessPartnersRepositoryImpl repositoryQuery,
                                     ConnectionPointsService connectionPointService,
                                     ConnectionPointRepository connectionPointRepository,
                                     ContractsService contractsService,
                                     EventListenerComponent<KafkaSchema<BusinessPartnerSchema>> eventListenerComponent) {
    this.businessPartnerRepository = businessPartnerRepository;
    this.personMapper = personMapper;
    this.businessPartnerMapper = businessPartnerMapper;
    this.personRepository = personRepository;
    this.contractRepository = contractRepository;
    this.repositoryQuery = repositoryQuery;
    this.connectionPointService = connectionPointService;
    this.connectionPointRepository = connectionPointRepository;
    this.contractsService = contractsService;
    this.eventListenerComponent = eventListenerComponent;
  }

  @Override
  public Page<BusinessPartnerSearchResult> getBusinessPartners(Map<String, Object> searchParams, Pageable page) {
    Page<com.scheidbachmann.masterdata.entity.BusinessPartner> partners = repositoryQuery.search(searchParams, page);
    return partners.map(businessPartnerMapper::toSearchResult);
  }


  @Override
  public BusinessPartnerDto addBusinessPartner(BusinessPartnerDto businessPartnerDto) {

    com.scheidbachmann.masterdata.entity.BusinessPartner businessPartner = businessPartnerMapper.toEntity(businessPartnerDto);
    businessPartner = businessPartnerRepository.save(businessPartner);
    Person person = personMapper.toEntity(businessPartnerDto.getContactPerson());
    if (person != null) {
      person.setBusinessPartnerId(businessPartner.getId());
      personRepository.save(person);
    }
    BusinessPartnerDto dto = businessPartnerMapper.toDto(businessPartner);
    dto.setContactPerson(personMapper.toDto(person));

    BusinessPartnerSchema businessPartnerSchema=new BusinessPartnerSchema();

    BeanUtils.copyProperties(dto,businessPartnerSchema);

//    System.out.print("Class :" + businessPartner.getClass().getSimpleName() + "\n");

    KafkaSchema<BusinessPartnerSchema> schema = KafkaSchema.with(businessPartnerSchema, dto.getTenantId(), businessPartner.getClass().getSimpleName()+"Created");
    eventListenerComponent.sendToTopic(KafkaTopics.BUSINESS_PARTNER_TOPIC, schema);

    return dto;

  }

  @Override
  public BusinessPartnerDto updateBusinessPartner(BusinessPartnerDto businessPartnerDto) {
    String id = businessPartnerDto.getId();
    if (!businessPartnerRepository.existsById(id)) {
      this.addBusinessPartner(businessPartnerDto);
    }
    BusinessPartner
      bpEntity =
      businessPartnerMapper.toEntity(businessPartnerDto);
    bpEntity.updateLastModified();
    final BusinessPartner entity =
      businessPartnerRepository.save(bpEntity);

    BusinessPartnerDto dto=businessPartnerMapper.toDto(entity);

    BusinessPartnerSchema businessPartnerSchema=new BusinessPartnerSchema();

    BeanUtils.copyProperties(dto,businessPartnerSchema);

    KafkaSchema<BusinessPartnerSchema> schema = KafkaSchema.with(businessPartnerSchema, dto.getTenantId(), entity.getClass().getSimpleName()+"Changed");
    eventListenerComponent.sendToTopic(KafkaTopics.BUSINESS_PARTNER_TOPIC, schema);



//    BusinessPartnerKafka event = BusinessPartnerKafka.with(dto,dto.getTenantId(),EventType.BusinessPartnerChanged);
//    eventListenerComponent.sendToTopic(KafkaTopics.BUSINESS_PARTNER_TOPIC, event, EventType.CHANGED);


    return businessPartnerMapper.toDto(entity);

  }

  @Override
  public BusinessPartnerDto getBusinessPartnerById(String id) {
    return businessPartnerMapper.toDto(businessPartnerRepository.findById(id).orElse(null));
  }


  @Override
  public Set<BusinessPartnerNameId> getBusinessPartnersNames() {
    return businessPartnerMapper.toNameIds(businessPartnerRepository.getBusinessPartnersList());
  }
  @Override
  public Set<BusinessPartnerNameId> getBusinessPartnersNamesByCPType(String type) {
    return businessPartnerMapper.toNameIds(businessPartnerRepository.getBusinessPartnerListByCPType(ConnectionPointTypeEnum.valueOf(type)));
  }

  @Override
  public Set<String> getTenants() {
    return businessPartnerRepository.getTenants();
  }

  @Override
  public void deleteBusinessPartners(List<String> ids) {
    List<BusinessPartner> businessPartners = businessPartnerRepository.findAllById(ids);
    for (BusinessPartner businessPartner : businessPartners) {
      List<Contract> contracts = contractRepository.findAll();
      for (Contract contract : contracts) {
        if (contract.getConsumerId().equals(businessPartner.getId()) || contract.getSupplierId().equals(businessPartner.getId())) {
          if (!(contract.getContractEnd().isAfter(contract.getContractStart()) && contract.getContractEnd().isAfter(LocalDate.now()))) {
            // Update deletedAt field to current dateTime
            businessPartner.setDeletedAt(LocalDateTime.now());

            BusinessPartnerDto dto = businessPartnerMapper.toDto(businessPartner);
            BusinessPartnerSchema businessPartnerSchema = new BusinessPartnerSchema();
            BeanUtils.copyProperties(dto, businessPartnerSchema);

            KafkaSchema<BusinessPartnerSchema> schema = KafkaSchema.with(businessPartnerSchema, dto.getTenantId(), businessPartner.getClass().getSimpleName() + "Deleted");
            eventListenerComponent.sendToTopic(KafkaTopics.BUSINESS_PARTNER_TOPIC, schema);

            // delete all corresponding connection points
            connectionPointService.deleteConnectionPoints(connectionPointRepository.getIdsByBusinessPartner(businessPartner.getId()));
            contractsService.deleteContracts(contractRepository.getIdsByBusinessPartner(businessPartner.getId()));
          }
          else {
            throw new IllegalStateException("Contract is still valid.");
          }
        }
      }
    }

    // Save or update the Business Partners
    businessPartnerRepository.saveAll(businessPartners);
  }

  @Override
  public Integer getProviderIdUnderHundred(){
    return businessPartnerRepository.getProviderIdUnderHundred();
  }
  @Override
  public Integer getProviderIdAboveHundred(){
    return businessPartnerRepository.getProviderIdAboveHundred();
  }

  @Override
  public Page<BusinessPartnerExport> getBusinessPartnersList(Map<String, Object> searchParams, Pageable page) {
    Page<com.scheidbachmann.masterdata.entity.BusinessPartner> BusinessPartners = repositoryQuery.search(searchParams, page);
    return BusinessPartners.map(businessPartnerMapper::toExport);
  }


}
