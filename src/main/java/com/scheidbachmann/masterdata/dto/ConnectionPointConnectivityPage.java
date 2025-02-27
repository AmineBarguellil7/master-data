package com.scheidbachmann.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * @author KaouechHaythem
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionPointConnectivityPage {

  @JsonProperty("totalElements")
  private Long totalElements;

  @JsonProperty("totalPages")
  private Integer totalPages;

  @JsonProperty("pageNumber")
  private Integer pageNumber;

  @JsonProperty("pageSize")
  private Integer pageSize;

  @JsonProperty("records")
  @Valid
  private List<ConnectionPointConnectivityDto> records = null;
}
