/**
 * Created By Amine Barguellil
 * Date : 5/24/2024
 * Time : 4:32 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.service.impl;



import com.scheidbachmann.masterdata.dto.ConnectionPointDto;
import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.entity.Contract;
import com.scheidbachmann.masterdata.entity.Sensor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;


@Service
public class ExcelExportService {



    public byte[] generateBusinessPartnerExcel(Set<BusinessPartner> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("Business Partners");

        var headerRow = sheet.createRow(0);
//        var cell = headerRow.createCell(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Partner Number");
        headerRow.createCell(2).setCellValue("Provider ID");
        headerRow.createCell(3).setCellValue("Tenant ID");
        headerRow.createCell(4).setCellValue("Name");
        headerRow.createCell(5).setCellValue("Country Code");
        headerRow.createCell(6).setCellValue("City");
        headerRow.createCell(7).setCellValue("Currency");
        headerRow.createCell(8).setCellValue("Business Partner Type");
        headerRow.createCell(9).setCellValue("Revision");
        headerRow.createCell(10).setCellValue("Contact Person Name");


        // Populate data rows
        int rowNum = 1;
        for (BusinessPartner businessPartner : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(businessPartner.getId());
            row.createCell(1).setCellValue(businessPartner.getPartnerNumber());
            row.createCell(2).setCellValue(businessPartner.getProviderId());
            row.createCell(3).setCellValue(businessPartner.getTenantId());
            row.createCell(4).setCellValue(businessPartner.getName());
            row.createCell(5).setCellValue(businessPartner.getCountryCode());
            row.createCell(6).setCellValue(businessPartner.getCity());
            row.createCell(7).setCellValue(businessPartner.getCurrency());
            row.createCell(8).setCellValue(businessPartner.getType() != null ? businessPartner.getType().toString() : "");
            row.createCell(9).setCellValue(businessPartner.getRevision());
            row.createCell(10).setCellValue(businessPartner.getContactPerson() != null ? businessPartner.getContactPerson().getFirstName() : "");
        }


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }


    public byte[] generateConnectionPointExcel(Set<ConnectionPoint> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("Connection Points");

        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Revision");
        headerRow.createCell(2).setCellValue("Type");
        headerRow.createCell(3).setCellValue("Name");
        headerRow.createCell(4).setCellValue("Location ID");
        headerRow.createCell(5).setCellValue("Facility ID");
        headerRow.createCell(6).setCellValue("Cell ID");
        headerRow.createCell(7).setCellValue("Operator ID");
        headerRow.createCell(8).setCellValue("Carpark Type");
        headerRow.createCell(9).setCellValue("Last Modified");
        headerRow.createCell(10).setCellValue("Order Number");
        headerRow.createCell(11).setCellValue("Technical Place");
        headerRow.createCell(12).setCellValue("Activated At");
        headerRow.createCell(13).setCellValue("Other");
        headerRow.createCell(14).setCellValue("With Leave Loop");
        headerRow.createCell(15).setCellValue("Tenant Name");
        headerRow.createCell(16).setCellValue("Geometry Path");
        headerRow.createCell(17).setCellValue("Keycloak Inbound User");
        headerRow.createCell(18).setCellValue("Business Partner ID");

        // Populate data rows
        int rowNum = 1;
        for (ConnectionPoint connectionPoint : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(connectionPoint.getId());
            row.createCell(1).setCellValue(connectionPoint.getRevision() != null ? connectionPoint.getRevision() : 0);
            row.createCell(2).setCellValue(connectionPoint.getType() != null ? connectionPoint.getType().toString() : "");
            row.createCell(3).setCellValue(connectionPoint.getName());
            row.createCell(4).setCellValue(connectionPoint.getLocationId());
            row.createCell(5).setCellValue(connectionPoint.getFacilityId());
            row.createCell(6).setCellValue(connectionPoint.getCellId());
            row.createCell(7).setCellValue(connectionPoint.getOperatorId());
            row.createCell(8).setCellValue(connectionPoint.getCarparkType() != null ? connectionPoint.getCarparkType().toString() : "");
            row.createCell(9).setCellValue(connectionPoint.getLastModified() != null ? connectionPoint.getLastModified().toString() : "");
            row.createCell(10).setCellValue(connectionPoint.getOrderNumber());
            row.createCell(11).setCellValue(connectionPoint.getTechnicalPlace());
            row.createCell(12).setCellValue(connectionPoint.getActivatedAt() != null ? connectionPoint.getActivatedAt().toString() : "");
            row.createCell(13).setCellValue(connectionPoint.getOther());
            row.createCell(14).setCellValue(connectionPoint.isWithLeaveLoop());
            row.createCell(15).setCellValue(connectionPoint.getTenantName());
            row.createCell(16).setCellValue(connectionPoint.getGeometryPath());
            row.createCell(17).setCellValue(connectionPoint.isKeycloakInboundUser());
            row.createCell(18).setCellValue(connectionPoint.getBusinessPartnerId());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }


    public byte[] generateContractExcel(Set<Contract> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("Contracts");

        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Revision");
        headerRow.createCell(2).setCellValue("Priority Level");
        headerRow.createCell(3).setCellValue("Supplier Connection Point Selection");
        headerRow.createCell(4).setCellValue("Consumer Connection Point Selection");
        headerRow.createCell(5).setCellValue("Contract Start");
        headerRow.createCell(6).setCellValue("Contract End");
        headerRow.createCell(7).setCellValue("Service Name");
        headerRow.createCell(8).setCellValue("Service ID");
        headerRow.createCell(9).setCellValue("Consumer ID");
        headerRow.createCell(10).setCellValue("Supplier ID");

        // Populate data rows
        int rowNum = 1;
        for (Contract contract : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(contract.getId());
            row.createCell(1).setCellValue(contract.getRevision());
            row.createCell(2).setCellValue(contract.getPriorityLevel() != null ? contract.getPriorityLevel().toString() : "");
            row.createCell(3).setCellValue(contract.getSupplierConnectionPointSelection() != null ? contract.getSupplierConnectionPointSelection().toString() : "");
            row.createCell(4).setCellValue(contract.getConsumerConnectionPointSelection() != null ? contract.getConsumerConnectionPointSelection().toString() : "");
            row.createCell(5).setCellValue(contract.getContractStart() != null ? contract.getContractStart().toString() : "");
            row.createCell(6).setCellValue(contract.getContractEnd() != null ? contract.getContractEnd().toString() : "");
            row.createCell(7).setCellValue(contract.getServiceName());
            row.createCell(8).setCellValue(contract.getServiceId());
            row.createCell(9).setCellValue(contract.getConsumerId());
            row.createCell(10).setCellValue(contract.getSupplierId());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }


    public byte[] generateSensorExcel(Set<Sensor> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("Sensors");

        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Revision");
        headerRow.createCell(2).setCellValue("Serial Number");
        headerRow.createCell(3).setCellValue("Lane Number");
        headerRow.createCell(4).setCellValue("Direction");
        headerRow.createCell(5).setCellValue("Location ID");
        headerRow.createCell(6).setCellValue("Device Name");
        headerRow.createCell(7).setCellValue("Tenant Name");
        headerRow.createCell(8).setCellValue("Connection Point ID");
        headerRow.createCell(9).setCellValue("API Key");

        // Populate data rows
        int rowNum = 1;
        for (Sensor sensor : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sensor.getId());
            row.createCell(1).setCellValue(sensor.getRevision() != null ? sensor.getRevision() : 0);
            row.createCell(2).setCellValue(sensor.getSerialNumber());
            row.createCell(3).setCellValue(sensor.getLaneNumber());
            row.createCell(4).setCellValue(sensor.getDirection() != null ? sensor.getDirection().toString() : "");
            row.createCell(5).setCellValue(sensor.getLocationId());
            row.createCell(6).setCellValue(sensor.getDeviceName());
            row.createCell(7).setCellValue(sensor.getTenantName());
            row.createCell(8).setCellValue(sensor.getConnectionPointId());
            row.createCell(9).setCellValue(sensor.getApiKey());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }


}

