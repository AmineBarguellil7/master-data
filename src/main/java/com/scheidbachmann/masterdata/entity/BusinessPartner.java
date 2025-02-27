/**
 * Created By Amine Barguellil
 * Date : 2/14/2024
 * Time : 8:30 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.entity;


import com.scheidbachmann.masterdata.enums.BusinessPartnerType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BUSINESS_PARTNER")
public class BusinessPartner {

  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String partnerNumber;

  private Integer providerId;

  private String tenantId;

  private String name;

  private String countryCode;


  private String city;

  private String currency;

  private boolean switchOffExit;


  @Enumerated(EnumType.STRING)
  @Column(length = 40)
  private BusinessPartnerType type;

  @Version
  private Long revision;

  private LocalDateTime lastModified;
  @OneToOne(mappedBy = "businessPartner", cascade = CascadeType.ALL)
  private Person contactPerson;


  @OneToMany(mappedBy = "businessPartner", cascade = CascadeType.ALL)
  private Set<ConnectionPoint> connectionPoints = new HashSet<>();

  @OneToMany(mappedBy = "consumer")
  private Set<Contract> consumerContracts = new HashSet<>();

  @OneToMany(mappedBy = "supplier")
  private Set<Contract> supplierContracts = new HashSet<>();

  @Column(name = "DELETED_AT")
  private LocalDateTime deletedAt=null;
  public void updateLastModified() {
    this.lastModified = LocalDateTime.now();
  }

  @PrePersist
  public void prePersist() {
    updateLastModified();
  }

  @PreUpdate
  public void preUpdate() {
    updateLastModified();
  }


}
