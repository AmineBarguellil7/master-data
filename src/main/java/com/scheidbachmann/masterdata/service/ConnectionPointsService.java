package com.scheidbachmann.masterdata.service;

import com.scheidbachmann.masterdata.dto.ConnectionPointDto;
import com.scheidbachmann.masterdata.dto.ConnectionPointExport;
import com.scheidbachmann.masterdata.dto.ConnectionPointSearchResult;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.enums.CarparkTypeEnum;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConnectionPointsService {

  List<ConnectionPointSearchResult> getConnectionPointsByBPId(String BPid);

  List<ConnectionPointSearchResult> getConnectionPointsByBPIdAndType(String BPid, String type);

  ConnectionPointDto getConnectionPoint(String id);

  ConnectionPointDto add(ConnectionPointDto connectionPointDto);


  @Transactional
  ConnectionPointDto saveConnectionPoint(ConnectionPointDto body) throws InterruptedException;


  Page<ConnectionPointSearchResult> getConnectionPoints(Map<String, Object> searchParams, Pageable page);

//  List<Map<String, String>> testConnectionPointsByBusinessPartnerName(String name);


//  List<ConnectionPointTypeEnum> getConnectionPointsTypes();

//  List<CarparkTypeEnum> getCarparkTypes();


  void deleteConnectionPoints(List<String>ids);


    /**
       * check unique location id in case of connection point of type facility
       *
       //* @param cp the connection point to add or update
       */
    boolean checkUniqueLocationId(String id , String locationId);



  Page<ConnectionPointExport> getConnectionPointsList(Map<String, Object> searchParams, Pageable page);

}
