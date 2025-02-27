/**
 * Created By Amine Barguellil
 * Date : 5/22/2024
 * Time : 12:32 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.kafka.config.ConnectionPointToTrxMgr;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.kafka.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ConnectionPointToTrxMgrSchema implements Schema {
    @NotNull
    @Size(max = 100)
    private String locationId;

    @NotNull
    private Boolean withLeaveLoop = false;

    @NotNull
    private String tenant;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public long getRevision() {
        return 0;
    }
}
