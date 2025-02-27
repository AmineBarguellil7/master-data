/**
 * Created By Amine Barguellil
 * Date : 2/15/2024
 * Time : 10:44 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.enums.SalutationEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDto {

  /**
   * Internal identifier of person.
   */
  @NotNull
  @Valid
  private String id;

  @NotNull
  @Size(max = 80)
  private String firstName;


  @NotNull
  @Size(max = 80)
  private String surName;


  @NotNull
  @Valid
  private SalutationEnum salutation;


  @Size(max = 80)
  private String personalTitle;


  @NotNull
  @Size(max = 100)
  @Email
  private String email;


  @Size(max = 100)
  private String phone;
  /**
   * The @Valid annotation is used on the nestedObject property. This annotation indicates that the nested object (address in this case) should also be validated.
   */
  @Valid
  private AddressDto address;


  /**
   * Internal revision number for optimistic locking
   */
  @NotNull
  private Long revision;

  private String businessPartnerId;

  private BusinessPartnerDto businessPartner;


}
