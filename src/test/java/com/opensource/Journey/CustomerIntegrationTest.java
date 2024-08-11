package com.opensource.Journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.opensource.Customer.Customer;
import com.opensource.Customer.CustomerRegistrationRequest;
import com.opensource.Customer.UpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.*;

// this class is integration test
// so surefire plugin is used to execute the unit test so we want to set the class name as
// CustomerIT (Integration Test)
// or else we want to change in pom.xml file to exclude the integration test class from  the surefire plugin test
// for that check in the pom.xml file

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final  String CUSTOMER_URI = "/api/v1/customers";
    @Test
    void canRegisterCustomer() {
     // create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() +"-"+ UUID.randomUUID() + "@gmail.com";
        int age = RANDOM.nextInt(1,99);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, age , email
        );

        // send request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

////        get All Customer;

     List<Customer> allCustomer =    webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // it is used to send  the request
                .expectStatus()// we expecting to get good request of Ok
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


////     //   Make sure customer is present here
     Customer expectedCustomer  = new Customer(
             name, age, email
     );

     assertThat(allCustomer)
             .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
             .contains(expectedCustomer);

     int id = allCustomer
             .stream()
             .filter(c -> c.getEmail().equals(email))
             .map(Customer::getId)
             .findFirst()
             .orElseThrow();
     expectedCustomer.setId(id);
//     // get the customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // it is used to send  the request
                .expectStatus()// we expecting to get good request of Ok
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);

    }

    @Test
    void canDeleteCustomer() {
        // create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() +"-"+ UUID.randomUUID() + "@gmail.com";
        int age = RANDOM.nextInt(1,99);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, age , email
        );

        // send request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
//
////        get All Customer;

        List<Customer> allCustomer =    webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // it is used to send  the request
                .expectStatus()// we expecting to get good request of Ok
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        int id = allCustomer
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete the customer
        webTestClient.delete()
                        .uri(CUSTOMER_URI +"/{id}" ,id)
                                .accept(MediaType.APPLICATION_JSON)
                                        .exchange()
                                                .expectStatus()
                                                        .isOk();

     // get the customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // it is used to send  the request
                .expectStatus()// we expecting to get good request of Ok
                .isNotFound();


    }

    @Test
    void canUpdateCustomer() {
        // create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() +"-"+ UUID.randomUUID() + "@gmail.com";
        int age = RANDOM.nextInt(1,99);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, age , email
        );

        // send request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
//
////        get All Customer;

        List<Customer> allCustomer =    webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // it is used to send  the request
                .expectStatus()// we expecting to get good request of Ok
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        int id = allCustomer
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update the customer
        String newName = "ami";
        UpdateRequest updateRequest = new UpdateRequest(
                newName, null,null
        );

        webTestClient.put()
                .uri(CUSTOMER_URI +"/{id}" ,id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest),UpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get the customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // it is used to send  the request
                .expectStatus()// we expecting to get good request of Ok
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();
        Customer expected = new Customer(
                id,newName,age,email
        );
        assertThat(updatedCustomer).isEqualTo(expected);
    }
//    @Test
//    void canUpdateCustomerEmail() {
//        // create a registration request
//        Faker faker = new Faker();
//        Name fakerName = faker.name();
//        String name = fakerName.fullName();
//        String email = fakerName.lastName() +"-"+ UUID.randomUUID() + "@gmail.com";
//        int age = RANDOM.nextInt(1,99);
//        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
//                name, age , email
//        );
//
//        // send request
//        webTestClient.post()
//                .uri(CUSTOMER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request),CustomerRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
////
//////        get All Customer;
//
//        List<Customer> allCustomer =    webTestClient.get()
//                .uri(CUSTOMER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange() // it is used to send  the request
//                .expectStatus()// we expecting to get good request of Ok
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Customer>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//        int id = allCustomer
//                .stream()
//                .filter(c -> c.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        // update the customer
//        String  newEmail = "hilary@gmail.com";
//        UpdateRequest updateRequest = new UpdateRequest(
//                null, null,newEmail
//        );
//
//        webTestClient.put()
//                .uri(CUSTOMER_URI +"/{id}" ,id)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(updateRequest),UpdateRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        // get the customer by id
//        Customer updatedCustomer = webTestClient.get()
//                .uri(CUSTOMER_URI + "/{id}", id )
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange() // it is used to send  the request
//                .expectStatus()// we expecting to get good request of Ok
//                .isOk()
//                .expectBody(Customer.class)
//                .returnResult()
//                .getResponseBody();
//        Customer expected = new Customer(
//                id,name,age,newEmail
//        );
//        assertThat(updatedCustomer).isEqualTo(expected);
//    }
}
