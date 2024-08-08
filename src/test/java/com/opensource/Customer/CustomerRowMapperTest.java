package com.opensource.Customer;


import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {
    
    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(20);
        when(resultSet.getString("name")).thenReturn("ezhil");
        when(resultSet.getString("email")).thenReturn("ezhil@gmail.com");

        // When
        Customer customer = customerRowMapper.mapRow(resultSet,1);

        // Then
        Customer expected = new Customer(
                1,"ezhil",20,"ezhil@gmail.com"
        );
        assertThat(customer).isEqualTo(expected);
    }
}