/**
 * Created By Amine Barguellil
 * Date : 2/20/2024
 * Time : 11:04 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.entity;

import com.scheidbachmann.masterdata.enums.LicenseType;
import jakarta.persistence.*;
import lombok.*;


@Embeddable
@Data
public class ContractLicense {


  @Enumerated(EnumType.STRING)
  @Column(length = 50)
  private LicenseType licenseType;


  private Double price;


  private String currency;
}
