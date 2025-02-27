/**
 * Created By Amine Barguellil
 * Date : 2/21/2024
 * Time : 9:22 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.enums.LicenseType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractLicenseDto {

    @NotNull(message = "licenseType is required")
    private LicenseType licenseType;


    @NotBlank(message = "price is required")
    private Double price;


    @NotBlank(message = "currency is required")
    private String currency;
}
