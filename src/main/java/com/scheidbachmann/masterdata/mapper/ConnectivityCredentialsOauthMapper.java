package com.scheidbachmann.masterdata.mapper;

import com.scheidbachmann.masterdata.dto.ConnectivityCredentialsOauthDto;
import com.scheidbachmann.masterdata.entity.ConnectivityCredentialsOauth;
import org.mapstruct.Mapper;

/**
 * @author KaouechHaythem
 */
@Mapper(componentModel = "spring")
public interface ConnectivityCredentialsOauthMapper extends EntityMapper<ConnectivityCredentialsOauthDto, ConnectivityCredentialsOauth> {
}
