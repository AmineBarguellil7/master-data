/**
 * Created By Amine Barguellil
 * Date : 2/20/2024
 * Time : 3:32 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scheidbachmann.masterdata.enums.DirectionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {

  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Version
  private Long revision;


  private String serialNumber;


  private String laneNumber;

  @Enumerated(EnumType.STRING)
  @Column(length = 10)
  private DirectionEnum direction;


  private String locationId;


  private String deviceName;

  private String tenantName;

  private String apiKey;

  @Column(name = "CONNECTION_POINT_ID")
  private String connectionPointId;

  @ManyToOne
  @JoinColumn(name = "CONNECTION_POINT_ID", insertable = false, updatable = false)
  @JsonIgnore
  private ConnectionPoint connectionPoint;

  @Column(name = "DELETED_AT")
  private LocalDateTime deletedAt=null;

  @Override
  public String toString() {
    return "Sensor{" +
            "id='" + id + '\'' +
            ", revision=" + revision +
            ", serialNumber='" + serialNumber + '\'' +
            ", laneNumber='" + laneNumber + '\'' +
            ", direction=" + direction +
            ", locationId='" + locationId + '\'' +
            ", deviceName='" + deviceName + '\'' +
            ", tenantName='" + tenantName + '\'' +
            ", connectionPointId='" + connectionPointId + '\'' +
            ", deletedAt=" + deletedAt +
            '}';
  }
}
