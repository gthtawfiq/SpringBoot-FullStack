package com.amigoscode.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import exception.DuplicateResourceException;
import exception.RequestValidationException;
import exception.ResourceNotFound;

@Service
public class CustomerService {
	
	private final CustomerDao customerdao;

	// This For JPA --------------------------------------
	/*public CustomerService(@Qualifier("jpa") CustomerDao customerdao) {
		super();
		this.customerdao = customerdao;
	}*/
	//End Of For JPA ---------------------------------

	// This For JDBC ---------------------------------------
	public CustomerService(@Qualifier("jdbc") CustomerDao customerdao) {
		super();
		this.customerdao = customerdao;
	}
	// End Of For JDBC ---------------------------------------
	
	public List<Customer> getAllCustomers(){
		return customerdao.getCustomers();
	}
	
	public Customer getCustomer(Integer id) {
		return customerdao.selectCustomerById(id)
				.orElseThrow(()-> new ResourceNotFound("Customer with [%s] not found".formatted(id)));
	}
	public void addCustomer (CustomerRegistrationRequest customerRegistrationRequest) {
		if (customerdao.existsPersonWithEmail(customerRegistrationRequest.email())) {
			throw new DuplicateResourceException("Email already taken");
		}
		
		Customer customer = new Customer(
					customerRegistrationRequest.name(),
					customerRegistrationRequest.email(),
					customerRegistrationRequest.age()
				);
		customerdao.insertCustomer(customer);
	}
	public void deleteCustomer(Integer id) {
		if (!customerdao.existsPersonWithId(id)) {
			throw new ResourceNotFound("Customer with [%s] not found".formatted(id));
		}
		customerdao.deleteCustomerById(id);
	}
	
	public void updateCustomer(CustomerUpdateRequest customerUpdateRequest, Integer id) {
		
		
		Customer customer = getCustomer(id);
		boolean changes= false;
		
		if(customerUpdateRequest.name()!=null && !customerUpdateRequest.name().equals(customer.getName())) {
			customer.setName(customerUpdateRequest.name());
			changes=true;
		}
		if(customerUpdateRequest.age()!=null && !customerUpdateRequest.age().equals(customer.getAge())) {
			customer.setAge(customerUpdateRequest.age());
			
			changes=true;
			
		}
		if(customerUpdateRequest.email()!=null && !customerUpdateRequest.email().equals(customer.getEmail())) {
			if (customerdao.existsPersonWithEmail(customerUpdateRequest.email())) {
				throw new DuplicateResourceException("Email already taken");
			}
			customer.setEmail(customerUpdateRequest.email());
			changes=true;
		}
		if (!changes) {
			throw new RequestValidationException("no data changes found");
		}
		customerdao.updateCustomer(customer);
	}

	
	
	

}
