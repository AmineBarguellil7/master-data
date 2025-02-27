/**
 * Created By Amine Barguellil
 * Date : 2/29/2024
 * Time : 4:22 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionPointToTrxMgr {

    @NotNull
    @Size(max = 100)
    private String locationId;

    @NotNull
    private Boolean withLeaveLoop = false;

    @NotNull
    private String tenant;

}
