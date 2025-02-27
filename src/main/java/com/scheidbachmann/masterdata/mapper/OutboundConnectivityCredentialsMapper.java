package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.OutboundConnectivityCredentialsDto;
import com.scheidbachmann.masterdata.entity.OutboundConnectivityCredentials;
import org.mapstruct.Mapper;

/**
 * @author KaouechHaythem
 */
@Mapper(componentModel = "spring", uses = {ConnectivityCredentialsOauthMapper.class})
public interface OutboundConnectivityCredentialsMapper extends EntityMapper<OutboundConnectivityCredentialsDto, OutboundConnectivityCredentials> {
}
