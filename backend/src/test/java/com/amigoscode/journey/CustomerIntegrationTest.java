package com.amigoscode.journey;

import com.amigoscode.customer.*;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {
    
    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM=new Random();
    private static final String CUSTOMER_PATH = "/api/v1/customers";

    @Test
    void canRegisterACustomer() {
        //Create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@foobarhello123.com";
        int age = RANDOM.nextInt(1,100);
        Gender gender = age % 2==0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                "password", age,
                gender);
        //Send a Post request

        String JWTToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);
        //get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",JWTToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();



        
        int id = allCustomers
                .stream()
                .filter(c->c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        //make sure that customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                gender,
                age,
                List.of("ROLE_USER"),
                email

        );
        assertThat(allCustomers).contains(expectedCustomer);
        //get customer by id
        webTestClient.get()
                .uri(CUSTOMER_PATH +"/{customerId}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",JWTToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
                .isEqualTo(expectedCustomer);

    }

    @Test
    void canDeleteCustomer() {


        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@foobarhello123.com";
        int age = RANDOM.nextInt(1,100);
        Gender gender = age % 2==0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                "password",
                age,
                gender);
        CustomerRegistrationRequest request2 = new CustomerRegistrationRequest(
                name,
                email + ".ma",
                "password",
                age,
                gender);
        //Send a Post request to create customer 1
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Send a Post request to create customer 2
        String JWTToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);
        //get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",JWTToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allCustomers
                .stream()
                .filter(c->c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        //customer 2 deletes customer 1
        webTestClient.delete()
                .uri(CUSTOMER_PATH +"/{customerId}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",JWTToken))
                .exchange()
                .expectStatus()
                .isOk();

        //customer 2 gets customer by id
        webTestClient.get()
                .uri(CUSTOMER_PATH +"/{customerId}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",JWTToken))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@foobarhello123.com";
        int age = RANDOM.nextInt(1,100);
        Gender gender = age % 2==0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                "password", age,
                gender);

        //Send a Post request

        String JWTToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);
        //get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",JWTToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        //get Customer id
        int id = allCustomers
                .stream()
                .filter(c->c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        //update the customer

        String newName = "nameTest123547821022223ss3";
//        String newEmail = "email2023155079888s580015@gmail.com";
//        int newAge = 65;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName,
                null,
                null
        );

        webTestClient
                .put()
                .uri(CUSTOMER_PATH +"/{customerId}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",JWTToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest),CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        //get customer by id
        CustomerDTO udpatedCustomer = webTestClient.get()
                .uri(CUSTOMER_PATH + "/{customerId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",JWTToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        CustomerDTO expected = new CustomerDTO(
                id, newName,email, gender, age,List.of("ROLE_USER"),email

                );

        assertThat(udpatedCustomer).isEqualTo(expected);




    }
}
