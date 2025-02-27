package com.scheidbachmann.masterdata.repository;


import com.scheidbachmann.masterdata.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, String> {

}
