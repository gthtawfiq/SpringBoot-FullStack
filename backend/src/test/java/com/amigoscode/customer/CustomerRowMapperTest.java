package com.amigoscode.customer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CustomerRowMapperTest {



    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper rowMapper = new CustomerRowMapper();
        ResultSet rs = mock(ResultSet.class);
        Mockito.when(rs.getInt("id")).thenReturn(1);
        Mockito.when(rs.getString("name")).thenReturn("Alex");
        Mockito.when(rs.getString("email")).thenReturn("Alex@gmail.com");
        Mockito.when(rs.getInt("age")).thenReturn(19);
        Mockito.when(rs.getString("gender")).thenReturn("FEMALE");
        //When
        Customer actual = rowMapper.mapRow(rs, 1);
        //Then
        Customer expected =new Customer(
                1,
                "Alex",
                "Alex@gmail.com"
                , "password", 19,
                Gender.FEMALE);
        assertThat(actual).isEqualTo(expected);
    }
}