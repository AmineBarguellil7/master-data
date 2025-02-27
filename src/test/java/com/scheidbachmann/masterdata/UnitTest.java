/**
 * Created By Amine Barguellil
 * Date : 3/4/2024
 * Time : 3:09 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(basePackages = {"com.scheidbachmann.masterdata.repository"})
@EntityScan("com.scheidbachmann.masterdata.entity")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers()
@ExtendWith(PostgresSQLExtension.class)
public @interface UnitTest {
}
