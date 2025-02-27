package com.scheidbachmann.masterdata.mapper;



import com.scheidbachmann.masterdata.dto.SensorDto;
import com.scheidbachmann.masterdata.dto.SensorExport;
import com.scheidbachmann.masterdata.dto.SensorSearchResult;
import com.scheidbachmann.masterdata.entity.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SensorMapper  extends EntityMapper<SensorDto, Sensor> {
    @Override
    @Mapping(target = "connectionPoint", ignore = true)
    SensorDto toDto(Sensor sensor);


//    @Mapping(target = "baseUrl", source = "connectionPointConnectivity.baseUrl")
    @Mapping(target = "id", source = "id")
    SensorSearchResult toSearchResult(Sensor sensor);



    @Mapping(target = "id", source = "sensor.id")
    @Mapping(target = "revision", source = "sensor.revision")
    @Mapping(target = "serialNumber", source = "sensor.serialNumber")
    @Mapping(target = "laneNumber", source = "sensor.laneNumber")
    @Mapping(target = "direction", source = "sensor.direction")
    @Mapping(target = "locationId", source = "sensor.locationId")
    @Mapping(target = "deviceName", source = "sensor.deviceName")
    @Mapping(target = "tenantName", source = "sensor.tenantName")
    @Mapping(target = "connectionPointId", source = "sensor.connectionPointId")
    @Mapping(target = "apiKey", source = "sensor.apiKey")
    SensorExport toExport(Sensor sensor);

}
