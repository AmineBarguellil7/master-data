/**
 * Created By Amine Barguellil
 */


package com.scheidbachmann.masterdata.repository;


import com.scheidbachmann.masterdata.entity.BusinessPartner;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Set;


public interface BusinessPartnerRepository extends JpaRepository<BusinessPartner, String> {


  @Query("select case when count(bp)> 0 then true else false end from BusinessPartner bp where bp.id = :id and bp.tenantId is not null")
  boolean existsTenantId(@Param("id") String id);

  @Query("Select distinct b from BusinessPartner b where b.name is not null and b.deletedAt is null")
  Set<BusinessPartner> getBusinessPartnersList();
  @Query("SELECT DISTINCT bp FROM BusinessPartner bp WHERE bp.deletedAt is null and EXISTS " +
    "(SELECT 1 FROM bp.connectionPoints cp WHERE cp.type = :type and cp.deletedAt is null)")
  Set<BusinessPartner> getBusinessPartnerListByCPType(@Param("type")ConnectionPointTypeEnum type);

  @Query("Select distinct b.tenantId from BusinessPartner b where b.tenantId is not null and b.deletedAt is null")
  Set<String> getTenants();
  @Query(value = "SELECT MIN(seq) FROM (SELECT seq FROM generate_series(1, 100) AS seq EXCEPT SELECT provider_Id FROM business_partner WHERE provider_Id BETWEEN 1 AND 100 and DELETED_AT is null) AS t", nativeQuery = true)

  Integer getProviderIdUnderHundred();
  @Query(value = "SELECT MIN(seq) FROM (SELECT seq FROM generate_series(101, 999) AS seq EXCEPT SELECT provider_Id FROM business_partner WHERE provider_Id BETWEEN 101 AND 999 and DELETED_AT is null) AS t", nativeQuery = true)

  Integer getProviderIdAboveHundred();


}
