package com.amigoscode;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * --------------------Notice------------------------------------------------------------------------------------
 * we should never use @SpringBootTest for Unit Tests cuz this annotation load all Beans for example if we want to test CustomerJDBCDataAccessService class
 * And we use @SpringBootTest this will load all jdbc's Beans and as we can we just 2 objects :
 *   "private final JdbcTemplate jdbcTemplate; private final CustomerRowMapper customerRowMapper;"
 *  --------------------------------------------------------------------------------------------------------------
 *  BUT for Integration Tests feel free to use it but remember if we use Unit Tests DON'T USE IT.
 *
 *  ---------------------------------------------------------------------------------------------------------------
 *
 * */


public class TestcontainersTest extends AbstractTestconstainersUnitTest{
    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        //assertThat(postgreSQLContainer.isHealthy()).isTrue();

    }

}
