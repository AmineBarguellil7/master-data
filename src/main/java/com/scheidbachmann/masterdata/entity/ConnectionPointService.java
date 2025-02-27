package com.scheidbachmann.masterdata.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scheidbachmann.masterdata.enums.ServiceConsumerRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

/**
 * @author KaouechHaythem
 * services used at a connection point as consumer or supplier (or unsupported) links:  rel connectionPoint
 */
@Entity
@Table(name = "CONNECTIONP_SERVICE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionPointService {

  @Id
  @JdbcTypeCode(Types.VARCHAR)
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Version
  private Long revision;


  private String serviceName;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ServiceConsumerRoleEnum endpointRole;


  @Column(name = "CONNECTION_POINT_ID")
  private String connectionPointId;

  @Column(name = "SERVICE_ID")
  private String serviceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CONNECTION_POINT_ID", insertable = false, updatable = false)
  @JsonIgnore
  private ConnectionPoint connectionPoint;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SERVICE_ID", insertable = false, updatable = false)
  @JsonIgnore
  private Service service;


}

