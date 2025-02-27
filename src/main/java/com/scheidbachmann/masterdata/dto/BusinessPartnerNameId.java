/**
 * Created By Amine Barguellil
 * Date : 3/19/2024
 * Time : 2:25 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessPartnerNameId {
    private String id;

    private String name;

}
