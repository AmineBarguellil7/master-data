package com.scheidbachmann.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {

  @Size(max = 80)
  private String state;

  @Size(max = 80)
  private String city;

  @Size(max = 10)
  private String zipCode;

  @Size(max = 80)
  private String street;

  @Size(max = 10)
  private String streetNumber;

  @Size(max = 2)
  private String countryCode;
}
