package com.amigoscode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    @Mock//When we use this annotation we need initialize------------
    private CustomerRepository customerRepository;
    private  AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);//This is Initialize of @Mock /**********************
        underTest=new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getCustomers() {
        //When
        underTest.getCustomers();
        //Then
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        //Given

        Integer id = 1;
        //When
        underTest.selectCustomerById(id);
        //Then
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        String email = "Foo@gmail.com";
        Customer customer = new Customer(
                "Test",
                email,
                "password", 23, Gender.MALE);
        //When
        underTest.insertCustomer(customer);
        //Then
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        String email = "Foo@gmail.com";

        //When
        underTest.existsPersonWithEmail(email);
        //Then
        Mockito.verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomerById() {
        //Given
        Integer id = 1;

        //When

        underTest.deleteCustomerById(id);
        //Then
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void existsPersonWithId() {
        //Given
        Integer id = 1;
        //When
        underTest.existsPersonWithId(id);

        //Then
        Mockito.verify(customerRepository).existsById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        String email = "Foo@gmail.com";
        Customer customer = new Customer(
                "Test",
                email,
                "password", 23, Gender.MALE);
        //When
        underTest.updateCustomer(customer);
        //Then
        Mockito.verify(customerRepository).save(customer);
    }
}