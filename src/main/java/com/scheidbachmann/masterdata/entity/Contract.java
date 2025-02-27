/**
 * Created By Amine Barguellil
 * Date : 2/14/2024
 * Time : 9:59 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scheidbachmann.masterdata.enums.PriorityLevel;
import com.scheidbachmann.masterdata.enums.SelectionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract {


  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Version
  private Long revision;


  @AttributeOverrides({
    @AttributeOverride(name = "licenseType", column = @Column(name = "SUPPLIER_LICENSE_TYPE")),
    @AttributeOverride(name = "price", column = @Column(name = "SUPPLIER_PRICE")),
    @AttributeOverride(name = "currency", column = @Column(name = "SUPPLIER_CURRENCY"))
  })
  @Embedded
  private ContractLicense supplierLicense;


  @AttributeOverrides({
    @AttributeOverride(name = "licenseType", column = @Column(name = "CONSUMER_LICENSE_TYPE")),
    @AttributeOverride(name = "price", column = @Column(name = "CONSUMER_PRICE")),
    @AttributeOverride(name = "currency", column = @Column(name = "CONSUMER_CURRENCY"))
  })
  @Embedded
  private ContractLicense consumerLicense;

  @Enumerated(EnumType.STRING)
  @Column(length = 50)
  private PriorityLevel priorityLevel;

  @Enumerated(EnumType.STRING)
  @Column(length = 50)
  private SelectionType supplierConnectionPointSelection;


  @Enumerated(EnumType.STRING)
  @Column(length = 50)
  private SelectionType consumerConnectionPointSelection;


  private LocalDate contractStart;

  private LocalDate contractEnd;

  private String serviceName;

  @Column(name = "SERVICE_ID")
  private String serviceId;

  @Column(name = "CONSUMER_ID")
  private String consumerId;

  @Column(name = "SUPPLIER_ID")
  private String supplierId;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SERVICE_ID", insertable = false, updatable = false)
  @JsonIgnore
  private Service service;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "CONSUMER_ID", insertable = false, updatable = false)
  @JsonIgnore
  private BusinessPartner consumer;


  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "SUPPLIER_ID", insertable = false, updatable = false)
  @JsonIgnore
  private BusinessPartner supplier;

  @Column(name = "DELETED_AT")
  private LocalDateTime deletedAt=null;

  @ManyToMany()
  @JoinTable(
    name = "consumer",
    joinColumns = @JoinColumn(name = "contract_id"),
    inverseJoinColumns = @JoinColumn(name = "connection_point_id")
  )
  private Set<ConnectionPoint> consumerConnectionPoints = new HashSet<>();


  @ManyToMany()
  @JoinTable(
    name = "supplier",
    joinColumns = @JoinColumn(name = "contract_id"),
    inverseJoinColumns = @JoinColumn(name = "connection_point_id")
  )
  private Set<ConnectionPoint> supplierConnectionPoints = new HashSet<>();


}
