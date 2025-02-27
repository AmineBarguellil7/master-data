
/**
 * Created By Amine Barguellil
 * Date : 2/20/2024
 * Time : 11:18 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

/**
 * @author KaouechHaythem
 * an admin-ui service
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
  @Id
  @JdbcTypeCode(Types.VARCHAR)
  private String id;

  private String name;

  private String subName;
  @OneToMany(mappedBy = "service")
  private Set<ConnectionPointService> connectionPointServices = new HashSet<>();

  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
  private Set<Contract> contracts = new HashSet<>();

}
