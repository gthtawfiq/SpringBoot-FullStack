package com.amigoscode.customer;

import com.amigoscode.AbstractTestconstainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestconstainersUnitTest {
    /**
     *----------------- @Autowired : Saying this is same thing as creating a constructor like in this example :--------------

     public CustomerRepositoryTest(CustomerRepository underTest) {
     this.underTest = underTest;
     }


     */

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;


    @BeforeEach
    void setUp() {
        underTest.deleteAll();//Because when we run the application we create Customer to check look the code in "SpringBootExampleApplication"
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                18

        );
        underTest.save(customer);

        //When
        boolean actual = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isTrue();
    }
    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        boolean actual = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isFalse();
    }

    /**--------------------------- Testing for existsCustomerById(){} ---------------
       for me, I did use jpa and didn't create a method like existsCustomerByEmail() in CustomerRepository

     **/

   /* @Test
    void existsCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                23
        );
        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        boolean actual = underTest.existsCustomerById(id);
        //Then
        assertThat(actual).isTrue();
    }*/
//    @Test
//    void existsCustomerByIdFailsWhenIdNotPresent() {
//        //Given
//
//        int id = -1;
//
//        //When
//        boolean actual = underTest.existsCustomerById(id);
//        //Then
//        assertThat(actual).isFalse();
//    }
}