package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.InboundConnectivityCredentialsDto;
import com.scheidbachmann.masterdata.entity.InboundConnectivityCredentials;
import org.mapstruct.Mapper;

/**
 * @author KaouechHaythem
 */
@Mapper(componentModel = "spring")
public interface InboundConnectivityCredentialsMapper extends EntityMapper<InboundConnectivityCredentialsDto,InboundConnectivityCredentials> {
}
