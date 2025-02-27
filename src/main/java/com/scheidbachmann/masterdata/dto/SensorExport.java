/**
 * Created By Amine Barguellil
 * Date : 5/30/2024
 * Time : 3:40 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.enums.DirectionEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorExport {
    private String id;

    @NotNull
    private Long revision;

    @NotBlank(message = "serialNumber is required")
    @Size(max = 36)
    private String serialNumber;


    @NotBlank(message = "laneNumber is required")
    @Size(max = 10)
    private String laneNumber;

    @NotNull(message = "direction is required")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(length = 10)
    private DirectionEnum direction;

    @NotBlank(message = "locationId is required")
    @Size(max = 100)
    private String locationId;


    @NotBlank(message = "deviceName is required")
    private String deviceName;


    private String tenantName;


    private String connectionPointId;

    private String apiKey;

}
