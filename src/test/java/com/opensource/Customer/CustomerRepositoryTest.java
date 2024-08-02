package com.opensource.Customer;

import com.opensource.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainer {
    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = faker.internet().safeEmailAddress() +"-"+UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.save(customer);

        // When
        var actual = underTest.existsCustomerByEmail(email);


        // Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerByEmailFailsWhenNotPresent() {
        // Given
        String email = faker.internet().safeEmailAddress()+ "-"+ UUID.randomUUID();

        // When
        var actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isFalse();

    }

    @Test
    void existsCustomerById() {
        // Given
        String email = faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                20,
                email
        );
        underTest.save(customer);
        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsCustomerById(id);


        // Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerByIdFailsWhenNotPresent() {
        // Given
        int id = -1;


        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isFalse();

    }
}