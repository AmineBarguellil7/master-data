package com.scheidbachmann.masterdata.repository;



import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ConnectionPointRepository extends JpaRepository<ConnectionPoint, String> {
  @Query("SELECT cp FROM ConnectionPoint cp WHERE cp.locationId = :locationId AND cp.deletedAt IS NULL")
  List<ConnectionPoint> findAllByLocationId(@Param("locationId") String locationId);


//  @Query("SELECT cp.connectionPointConnectivity.host  "
//    + " FROM ConnectionPoint cp "
//    + " WHERE lower(cp.locationId) LIKE :locationId")
//  String findHostByLocationId(String locationId);


//  @Query("SELECT new map(c.businessPartner.id as id, c.businessPartner.name as name) "
//          + "FROM ConnectionPoint c "
//          + "INNER JOIN c.businessPartner b "
//          + "WHERE LOWER(b.name) LIKE :name")
//  List<Map<String, String>> getBusinessPartnersInfoByName(@Param("name") String name);


  @Query("SELECT cp " +
          "FROM ConnectionPoint cp " +
          "INNER JOIN cp.businessPartner bp " +
          "WHERE bp.id = :bpId and cp.type = :type and  cp.deletedAt is null")
  List<ConnectionPoint> getConnectionPointsByBPIdAndType(@Param("bpId") String BPid, @Param("type") ConnectionPointTypeEnum type);
  @Query("SELECT cp " +
    "FROM ConnectionPoint cp " +
    "INNER JOIN cp.businessPartner bp " +
    "WHERE bp.id = :bpId and cp.deletedAt is null")
  List<ConnectionPoint> getConnectionPointsByBPId(@Param("bpId") String BPid);

  @Query("SELECT c.id"
    + " FROM ConnectionPoint c "
    + " WHERE c.businessPartnerId =:id"
    + " AND c.deletedAt IS NULL")
    List<String> getIdsByBusinessPartner(@Param("id")String id);


  @Query("SELECT DISTINCT c FROM ConnectionPoint c WHERE c.deletedAt IS NULL")
  Set<ConnectionPoint> getConnectionPoints();


}
