package com.opensource.Customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
// This class is Unit Testing
class CustomerJPAServiceTest {
    private CustomerJPAService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPAService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        // Then
        verify(customerRepository)
                .findAll();

        // Then

    }

    @Test
    void selectCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.selectCustomerById(id);

        // Then
        verify(customerRepository)
                .findById(id);

    }

    @Test
    void deleteCustomer() {
        // Given
        int id =1;

        // When
        underTest.deleteCustomer(id);

        // Then
        verify(customerRepository)
                .deleteById(id);

    }

    @Test
    void existPersonWithEmail() {
        // Given
        String email = "ezhil@gmail.com";

        // When
        underTest.existPersonWithEmail(email);

        // Then
        verify(customerRepository)
                .existsCustomerByEmail(email);

    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(12,"ezhil", 22,"ezhil@gmail.com");



        // When
        underTest.updateCustomer(customer);

        // Then
        verify(customerRepository)
                .save(customer);
    }

    @Test
    void existPersonWithId() {
        // Given
        int id = 1;


        // When
        underTest.existPersonWithId(id);

        // Then
        verify(customerRepository)
                .existsCustomerById(id);

    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(1,"ezhil",22,"ezhil@gmail.com");
        // When
        underTest.insertCustomer(customer);

        // Then
        verify(customerRepository)
                .save(customer);

    }
}