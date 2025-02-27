package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.PersonDto;
import com.scheidbachmann.masterdata.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface PersonMapper extends EntityMapper<PersonDto, Person> {

  @Override
  @Mapping(target = "businessPartner", ignore = true)
  PersonDto toDto(Person person);
}
