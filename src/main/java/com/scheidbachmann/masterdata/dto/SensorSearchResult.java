/**
 * Created By Amine Barguellil
 * Date : 3/7/2024
 * Time : 9:03 AM
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
public class SensorSearchResult {


    private String id ;

    private String serialNumber;

    private String laneNumber;

    private DirectionEnum direction;

    private String locationId;

}
