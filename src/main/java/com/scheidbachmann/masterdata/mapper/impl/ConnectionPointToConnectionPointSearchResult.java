package com.scheidbachmann.masterdata.mapper.impl;

import com.scheidbachmann.masterdata.dto.ConnectionPointSearchResult;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.entity.ConnectionPointConnectivity;
import com.scheidbachmann.masterdata.entity.ConnectionPointService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class ConnectionPointToConnectionPointSearchResult {

  public ConnectionPointSearchResult toSearchResult(ConnectionPoint connectionPoint) {
    if ( connectionPoint == null ) {
      return null;
    }

    System.out.print("///////////////"+ connectionPoint);

    ConnectionPointSearchResult connectionPointSearchResult = new ConnectionPointSearchResult();

    connectionPointSearchResult.setBaseUrl( connectionPointConnectionPointConnectivityBaseUrl( connectionPoint ) );
    connectionPointSearchResult.setId( connectionPoint.getId() );
    connectionPointSearchResult.setType( connectionPoint.getType() );
    connectionPointSearchResult.setName( connectionPoint.getName() );
    if (connectionPoint.getBusinessPartner() !=null) {
      connectionPointSearchResult.setOperatorId( connectionPoint.getBusinessPartner().getName() );
    }
//    connectionPointSearchResult.setOperatorId( connectionPoint.getBusinessPartner().getName() );
    Set<String> serviceNames = getServices(connectionPoint);
    connectionPointSearchResult.setConnectionPointServices(serviceNames);

    return connectionPointSearchResult;
  }

  private String connectionPointConnectionPointConnectivityBaseUrl(ConnectionPoint connectionPoint) {
    if ( connectionPoint == null ) {
      return null;
    }
    ConnectionPointConnectivity connectionPointConnectivity = connectionPoint.getConnectionPointConnectivity();
    if ( connectionPointConnectivity == null ) {
      return null;
    }
    String baseUrl = connectionPointConnectivity.getBaseUrl();
    if ( baseUrl == null ) {
      return null;
    }
    return baseUrl;
  }

  private Set<String> getServices(ConnectionPoint connectionPoint){
    return connectionPoint.getConnectionPointServices()
      .stream()
      .map(ConnectionPointService::getService)
      .filter(service -> service != null)
      .map(com.scheidbachmann.masterdata.entity.Service::getName)
      .collect(Collectors.toSet());
  }


  public List<ConnectionPointSearchResult> toSearchResults(List<ConnectionPoint> connectionPoints) {
    if ( connectionPoints == null ) {
      return null;
    }

    return connectionPoints.stream()
            .map(this::toSearchResult)
            .collect(Collectors.toList());
  }

}
