package com.opensource.Customer;

import com.github.javafaker.Faker;
import com.opensource.AbstractTestContainer;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCServiceTest extends AbstractTestContainer {
    private CustomerJDBCService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                faker.internet().safeEmailAddress() + " - "+ UUID.randomUUID()
        );
        underTest.insertCustomer(customer);
        // When
        List<Customer> actual = underTest.selectAllCustomers();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        String email = faker.internet().safeEmailAddress() + " - "+ UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
       Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent()
                .hasValueSatisfying(c -> {
                   assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        int id =-1;

        // When
        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();

    }



    @Test
    void existPersonWithEmail() {
        // Given
        String email = faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
            name,
            20,
            email
        );
        underTest.insertCustomer(customer);

        // When
        boolean actual = underTest.existPersonWithEmail(email);

        // Then
        assertThat(actual).isTrue();

    }

    @Test
    void existPersonWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email =faker.internet().safeEmailAddress() + "-"+ UUID.randomUUID();

        // When
        boolean actual = underTest.existPersonWithEmail(email);

        // Then
        assertThat(actual).isFalse();

    }

    @Test
    void existPersonWithId() {
        // Given
        String email = faker.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        boolean actual = underTest.existPersonWithId(id);

        // Then
        assertThat(actual).isTrue();

    }

    @Test
    void existPersonWithIdReturnsFalseWhenDoesNotExists() {
        // Given
        int id = -1;

        // When
        boolean actual = underTest.existPersonWithId(id);

        // Then
        assertThat(actual).isFalse();

    }

    @Test
    void deleteCustomer() {
        // Given
        String email = faker.internet().safeEmailAddress() +"-"+UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteCustomer(id);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();

    }

    @Test
    void updateCustomerName() {
        // Given
        String email = faker.internet().safeEmailAddress() +"-"+UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newName = "dane";


        // When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(newName);
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                }
        );

    }
    @Test
    void updateCustomerAge() {
        // Given
        String email = faker.internet().safeEmailAddress() +"-"+UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newAge = 100;


        // When
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getAge()).isEqualTo(newAge);
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                }
        );

    }
    @Test
    void updateCustomerEmail() {
        // Given
        String email = faker.internet().safeEmailAddress() +"-"+UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newEmail = faker.internet().safeEmailAddress() +"-"+UUID.randomUUID();
        // When
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);
        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                    assertThat(c.getEmail()).isEqualTo(newEmail);
                }
        );
    }

    @Test
    void updateCustomer() {
        // Given
        String email = faker.internet().safeEmailAddress() +"-"+UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer update = new Customer();
        update.setId(id);
        update.setName("dane");
        update.setAge(22);
        update.setEmail(UUID.randomUUID().toString());

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);

    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        String email = faker.internet().safeEmailAddress() +"-"+UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer update = new Customer();
        update.setId(id);
        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                }
        );

    }
}