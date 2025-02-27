/**
 * Created By Amine Barguellil
 * Date : 2/14/2024
 * Time : 10:17 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.entity;


import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Address {


  private String state;


  private String city;


  private String zipCode;


  private String street;


  private String streetNumber;


  private String countryCode;


}
