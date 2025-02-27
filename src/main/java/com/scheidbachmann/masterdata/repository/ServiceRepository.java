package com.scheidbachmann.masterdata.repository;

import com.scheidbachmann.masterdata.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @author KaouechHaythem
 */
public interface ServiceRepository extends JpaRepository<Service,String> {
}
