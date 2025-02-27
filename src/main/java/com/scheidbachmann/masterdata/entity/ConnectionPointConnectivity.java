package com.scheidbachmann.masterdata.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

/**
 * @author KaouechHaythem
 * connectivity properties for a connection point links: rel connectionPoint
 */
@Entity
@Table(name = "CONNECTIVITY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionPointConnectivity {

  @Id
  @JdbcTypeCode(Types.VARCHAR)
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Version
  private Long revision;


  private Integer port;

  private String host;

  private String baseUrl;

  private String requestFormat;

  private String responseFormat;

  private OutboundConnectivityCredentials outboundCredentials;

  private InboundConnectivityCredentials inboundCredentials;

  @Column(name = "CONNECTION_POINT_ID")
  private String connectionPointId;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "CONNECTION_POINT_ID", nullable = false, insertable = false, updatable = false)
  @JsonIgnore
  private ConnectionPoint connectionPoint;
}
