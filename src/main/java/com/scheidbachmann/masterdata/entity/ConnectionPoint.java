/**
 * Created By Amine Barguellil
 * Date : 2/14/2024
 * Time : 11:06 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scheidbachmann.masterdata.enums.CarparkTypeEnum;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CONNECTION_POINT")
@Cacheable(false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//it means that caching is disabled for all methods within the ConnectionPoint class. Each method call and its results won't be cached, and the method logic will be executed on every call, without retrieving results from a cache.
public class ConnectionPoint {

  @Id
  @JdbcTypeCode(Types.VARCHAR)
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;


  @Version
  private Long revision;


  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ConnectionPointTypeEnum type;

  private String name;

  private String locationId;

  private String facilityId;

  private String cellId;

  private String operatorId;


  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private CarparkTypeEnum carparkType;

  private LocalDateTime lastModified;


  @Column(length = 50)
  private String orderNumber;

  @Column(length = 50)
  private String technicalPlace;

  @Column(length = 50)
  private LocalDate activatedAt;

  @Column
  private String other;

  @Column
  private boolean withLeaveLoop;
  @Column
  private String tenantName;
  @Column
  private String geometryPath;

  @Column(name = "keyclaok_inbound_user", columnDefinition = "boolean default true")
  private boolean keycloakInboundUser;


  @Column(name = "BUSINESS_PARTNER_ID")
  private String businessPartnerId;


  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "connectionPoint")
  private ConnectionPointConnectivity connectionPointConnectivity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BUSINESS_PARTNER_ID", insertable = false, updatable = false)
  @JsonIgnore
  private BusinessPartner businessPartner;


  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "connectionPoint")
  private Set<ConnectionPointService> connectionPointServices = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "connectionPoint")
  private Set<Sensor> sensors = new HashSet<>();

  @JsonIgnore
  @ManyToMany(mappedBy = "consumerConnectionPoints")
  private Set<Contract> consumerContracts = new HashSet<>();


  @JsonIgnore
  @ManyToMany(mappedBy = "supplierConnectionPoints")

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


//  @Override
//  public String toString() {
//    return "ConnectionPoint{" +
//            "id='" + id + '\'' +
//            ", revision=" + revision +
//            ", type=" + type +
//            ", name='" + name + '\'' +
//            ", locationId='" + locationId + '\'' +
//            ", facilityId='" + facilityId + '\'' +
//            ", cellId='" + cellId + '\'' +
//            ", operatorId='" + operatorId + '\'' +
//            ", carparkType=" + carparkType +
//            ", lastModified=" + lastModified +
//            ", orderNumber='" + orderNumber + '\'' +
//            ", technicalPlace='" + technicalPlace + '\'' +
//            ", activatedAt=" + activatedAt +
//            ", other='" + other + '\'' +
//            ", withLeaveLoop=" + withLeaveLoop +
//            ", tenantName='" + tenantName + '\'' +
//            ", geometryPath='" + geometryPath + '\'' +
//            ", keycloakInboundUser=" + keycloakInboundUser +
//            ", businessPartnerId='" + businessPartnerId + '\'' +
//            ", connectionPointConnectivity=" + connectionPointConnectivity +
//            ", businessPartner=" + businessPartner +
//            ", connectionPointServices=" + connectionPointServices +
//            ", sensors=" + sensors +
//            ", consumerContracts=" + consumerContracts +
//            ", supplierContracts=" + supplierContracts +
//            ", deletedAt=" + deletedAt +
//            '}';
//  }
}
