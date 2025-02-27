/**
 * Created By Amine Barguellil
 * Date : 3/6/2024
 * Time : 3:25 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.dto;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionPointSearchResult {



    private String id;

    private ConnectionPointTypeEnum type;

    private String name;

    private String operatorId;

    private String baseUrl;

  private Set<String> connectionPointServices;


}
