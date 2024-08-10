package com.opensource.Customer;

import com.opensource.Exception.DuplicateResourceException;
import com.opensource.Exception.RequestValidationException;
import com.opensource.Exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// This is unit testing
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void getAllCustomer() {

        // When
        underTest.getAllCustomer();

        // Then
        verify(customerDAO)
                .selectAllCustomers();

    }

    @Test
    void canGetCustomer() {
        // Given
        int id = 12;
        Customer customer = new Customer(
                id,"ezhil",22,"ezhil@gmailc.om"
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(id);

        // Then
        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        // Given
        int id = 12;

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage( "customer with id  [%s] is not found".formatted(id));

    }

    @Test
    void addCustomer() {
        // Given
        String email = "ezhil@gmail.com";
        when(customerDAO.existPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            "ezhil", 22, email
        );

        // When
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
    }

    @Test
    void willThrowWhenEmailExistWhileAddingACustomer() {
        // Given
        String email = "ezhil@gmailc.com";
        when(customerDAO.existPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest register = new CustomerRegistrationRequest(
                "ezhil",22,email
        );

        // When
        assertThatThrownBy(()->underTest.addCustomer(register))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");
        // Then
        verify(customerDAO, never()).insertCustomer(any());


    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 4;
        when(customerDAO.existPersonWithId(id)).thenReturn(true);

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerDAO).deleteCustomer(id);

    }
    @Test
    void willThrowDeleteCustomerByIdNotExists() {
        // Given
        int id = 4;
        when(customerDAO.existPersonWithId(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                        .hasMessage("customer with id  [%s] is not found".formatted(id));

        // Then
        verify(customerDAO, never()).deleteCustomer(id);

    }


    @Test
    void canUpdateAllCustomerProperties() {
        // Given
        int id = 12;
        Customer customer = new Customer(
                id,"ezhil",22,"ezhil@gmailc.om"
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "ezhilhilarya@gmail.com";
        UpdateRequest updateRequest = new UpdateRequest(
                "ezhilh",21, newEmail
        );
        when(customerDAO.existPersonWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id,updateRequest);

        // Then
        ArgumentCaptor<Customer>  customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id = 12;
        Customer customer = new Customer(
                id,"ezhil",22,"ezhil@gmailc.om"
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));


        UpdateRequest updateRequest = new UpdateRequest(
                "ezhilh",null, null
        );


        // When
        underTest.updateCustomer(id,updateRequest);

        // Then
        ArgumentCaptor<Customer>  customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());

    }
    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int id = 12;
        Customer customer = new Customer(
                id,"ezhil",22,"ezhil@gmailc.om"
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "ezhilhilarya@gmail.com";
        UpdateRequest updateRequest = new UpdateRequest(
                null,null, newEmail
        );
        when(customerDAO.existPersonWithEmail(newEmail)).thenReturn(false);


        // When
        underTest.updateCustomer(id,updateRequest);

        // Then
        ArgumentCaptor<Customer>  customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);

    }
    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 12;
        Customer customer = new Customer(
                id,"ezhil",22,"ezhil@gmailc.om"
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));


        UpdateRequest updateRequest = new UpdateRequest(
                "null",24, null
        );


        // When
        underTest.updateCustomer(id,updateRequest);

        // Then
        ArgumentCaptor<Customer>  customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());

    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailAlreadyTaken() {
        // Given
        int id = 12;
        Customer customer = new Customer(
                id,"ezhil",22,"ezhil@gmailc.om"
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "ezhilhilarya@gmail.com";
        UpdateRequest updateRequest = new UpdateRequest(
                null,null, newEmail
        );
        when(customerDAO.existPersonWithEmail(newEmail)).thenReturn(true);


        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(customerDAO, never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenNoCustomerUpdateHasNOChanges() {
        // Given
        int id = 12;
        Customer customer = new Customer(
                id,"ezhil",22,"ezhil@gmailc.om"
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        UpdateRequest updateRequest = new UpdateRequest(
                customer.getName(),customer.getAge(), customer.getEmail()
        );

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes");
        // Then
        verify(customerDAO, never()).updateCustomer(any());

    }
}