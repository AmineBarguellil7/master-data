/**
 * Created By Amine Barguellil
 * Date : 2/14/2024
 * Time : 8:45 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scheidbachmann.masterdata.enums.SalutationEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PERSON")
@Getter
@Setter
public class Person {


  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String firstName;

  private String surName;

  @Enumerated(EnumType.STRING)
  private SalutationEnum salutation;

  private String personalTitle;

  private String email;

  private String phone;
  @Embedded
  private Address address;

  // If i update a person entity , i change the value of revision to another value different than the one in the database it will generate OptimisticLockException.
  @Version
  private Long revision;

  @Column(name = "BUSINESS_PARTNER_ID")
  private String businessPartnerId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BUSINESS_PARTNER_ID", insertable = false, updatable = false)
  @JsonIgnore
  private BusinessPartner businessPartner;


}
