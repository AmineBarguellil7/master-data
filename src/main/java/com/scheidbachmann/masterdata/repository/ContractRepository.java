package com.scheidbachmann.masterdata.repository;

import com.scheidbachmann.masterdata.entity.Contract;
import com.scheidbachmann.masterdata.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ContractRepository extends JpaRepository<Contract, String> {

  @Query("SELECT c.id"
    + " FROM Contract c "
    + " WHERE ( c.supplierId =:id"
    + " OR c.consumerId =:id)"
    + " AND c.deletedAt IS NULL")
  List<String> getIdsByBusinessPartner(@Param("id") String id);


  @Query("SELECT DISTINCT c FROM Contract c WHERE c.deletedAt IS NULL")
  Set<Contract> getContracts();

}
