package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)//Other way to initialize mocks
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();
    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao, passwordEncoder, customerDTOMapper);
        //AutoCloseable mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();
        //Then
        Mockito.verify(customerDao).getCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "alex",
                "AlexTesting@Gmail.com",
                "password", 20,

                Gender.MALE);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerDTO expected = customerDTOMapper.apply(customer);
        //When
        CustomerDTO actual = underTest.getCustomer(id);
        //Then
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //Given
        int id = 10;
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(()->underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Customer with [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        //Given
        String email = "AlexTesting@Gmail.com";
        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex",
                email,
                "password", 19,
                Gender.MALE);

        String passwordHash="c55554;f;ls;dff";
        Mockito.when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);

        //When
        underTest.addCustomer(request);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer captureCustomer = customerArgumentCaptor.getValue();
        assertThat(captureCustomer.getId()).isNull();
        assertThat(captureCustomer.getName()).isEqualTo(request.name());
        assertThat(captureCustomer.getEmail()).isEqualTo(request.email());
        assertThat(captureCustomer.getPassword()).isEqualTo(passwordHash);
        assertThat(captureCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void WillThrowWhenEmailExistsWhileAddingACustomer() {
        //Given
        String email = "AlexTesting@Gmail.com";
        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex",
                email,
                "password", 19,
                Gender.MALE);

        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
        //Then

        Mockito.verify(customerDao, Mockito.never()).insertCustomer(any());

    }
    @Test
    void deleteCustomer() {
        //Given
        Integer id = 10;

        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);
        //When
        underTest.deleteCustomer(id);
        //Then
        Mockito.verify(customerDao).deleteCustomerById(id);
    }
    @Test
    void WillThrowWhenDeletingACustomerWithIdNotExisting() {
        //Given
        Integer id = 10;

        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(false);
        //When
        assertThatThrownBy(()->underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                        .hasMessage("Customer with [%s] not found".formatted(id));
        //Then
        Mockito.verify(customerDao,Mockito.never()).deleteCustomerById(id);
    }

    @Test
    void canUpdateAllCustomersProperties() {
        //Given
        Integer id =10;
        String email = "alexandro@gmail.com";
        boolean changes = true;
        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                "password", 19,

                Gender.MALE);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "alexandro",
                email,
                22

        );

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);



        //When

        underTest.updateCustomer(request,id);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void canUpdateNameOfCustomer() {
        //Given
        Integer id =10;
        String email = "alexandro@gmail.com";

        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                "password", 19,

                Gender.MALE);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "alexandro",
                null,
                null

        );





        //When

        underTest.updateCustomer(request,id);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());


    }
    @Test
    void canUpdateAgeOfCustomer() {
        //Given
        Integer id =10;
        String email = "alexandro@gmail.com";

        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                "password", 19,

                Gender.MALE);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null,
                null,
                22

        );





        //When

        underTest.updateCustomer(request,id);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());


    }
    @Test
    void canUpdateEmailOfCustomer() {
        //Given
        Integer id =10;
        String email = "alexandro@gmail.com";
        boolean changes = true;
        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                "password", 19,

                Gender.MALE);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null,
                email,
                null

        );

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);



        //When

        underTest.updateCustomer(request,id);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(email);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());


    }

    @Test
    void WillThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        //Given
        Integer id =10;
        String email = "alexandro@gmail.com";
        boolean changes = true;
        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                "password", 19,

                Gender.MALE);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "alexandro",
                email,
                22

        );

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(true);



        //When

        assertThatThrownBy(()->underTest.updateCustomer(request,id))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        //Then


        Mockito.verify(customerDao,Mockito.never()).updateCustomer(any());


    }
    @Test
    void WillThrowWhenCustomerUpdateHasNoChanges() {
        //Given
        Integer id =10;
        String email = "alexandro@gmail.com";
        Customer customer = new Customer(
                id,
                "alex",
                "alex@gmail.com",
                "password", 19,

                Gender.MALE);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge()

        );




        //When

        assertThatThrownBy(()->underTest.updateCustomer(request,id))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        //Then


        Mockito.verify(customerDao,Mockito.never()).updateCustomer(any());

    }
}