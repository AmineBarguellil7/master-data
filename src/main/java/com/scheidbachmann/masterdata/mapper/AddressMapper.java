package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.AddressDto;
import com.scheidbachmann.masterdata.entity.Address;
import org.mapstruct.Mapper;

/**
 * @author KaouechHaythem
 */
@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDto, Address> {

}
