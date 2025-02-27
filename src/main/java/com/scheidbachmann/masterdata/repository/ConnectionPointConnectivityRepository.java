package com.scheidbachmann.masterdata.repository;


import com.scheidbachmann.masterdata.entity.ConnectionPointConnectivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConnectionPointConnectivityRepository extends JpaRepository<ConnectionPointConnectivity, String> {
    Page<ConnectionPointConnectivity> findAllByInboundCredentialsIsNotNullOrderById(Pageable pageable);

  @Query("SELECT distinct connectivity.port FROM ConnectionPointConnectivity connectivity   where  lower(connectivity.host) LIKE lower(:hostname)")
  List<Integer> getUsedPortsByHost(@Param("hostname") String hostname);

  @Query("SELECT distinct connectivity.port FROM ConnectionPointConnectivity connectivity where connectivity.connectionPoint.locationId LIKE  %:location ")
  Integer nextFreePortByLocation(String location);
}
