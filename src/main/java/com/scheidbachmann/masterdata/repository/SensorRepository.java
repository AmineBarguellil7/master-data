package com.scheidbachmann.masterdata.repository;

import com.scheidbachmann.masterdata.entity.Sensor;
import com.scheidbachmann.masterdata.enums.DirectionEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SensorRepository extends JpaRepository<Sensor, String> {
  Page<Sensor> findAllByIdIn(List<String> ids, Pageable pageable);

  Page<Sensor> findAllByConnectionPointId(String connectionPointId, Pageable pageable);

  Page<Sensor> findAllBySerialNumberContainingIgnoreCase(String serialNumber, Pageable pageable);

  @Query("SELECT s.serialNumber"
    + " FROM Sensor s "
    + " WHERE lower(s.serialNumber) LIKE %:serialNumber%")
  List<String> getSerialNumber(@Param("serialNumber") String serialNumber);


  Optional<Sensor> findBySerialNumber(String serialNumber);

  Optional<Sensor> findByLaneNumber(String laneNumber);



  @Query("SELECT s.id"
    + " FROM Sensor s "
    + " WHERE s.connectionPointId =:id"
    + " AND s.deletedAt IS NULL")
  List<String> getIdsByConnectionPoint(@Param("id") String id);

  boolean existsBySerialNumber(String serialNumber);

  boolean existsByLaneNumber(String LaneNumber);

  boolean existsByLaneNumberAndDirectionAndIdNot(String LaneNumber, DirectionEnum directionEnum , String id);



  @Query("SELECT DISTINCT s FROM Sensor s WHERE s.deletedAt IS NULL")
  Set<Sensor> getSensors();


}
