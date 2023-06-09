package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao{

	private final CustomerRepository customerRepository;
	
	public CustomerJPADataAccessService(CustomerRepository customerRepository) {
		this.customerRepository=customerRepository;
	}
	
	
	@Override
	public List<Customer> getCustomers() {
		// TODO Auto-generated method stub
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> selectCustomerById(Integer id) {
		// TODO Auto-generated method stub
		return customerRepository.findById(id);
	}


	@Override
	public void insertCustomer(Customer customer) {
		customerRepository.save(customer);
		
	}


	@Override
	public boolean existsPersonWithEmail(String email) {
		// TODO Auto-generated method stub
		return customerRepository.existsCustomerByEmail(email);
	}


	@Override
	public void deleteCustomerById(Integer id) {
		// TODO Auto-generated method stub
		 customerRepository.deleteById(id);;
	}


	@Override
	public boolean existsPersonWithId(Integer id) {
		// TODO Auto-generated method stub
		return customerRepository.existsById(id);
	}


	@Override
	public void updateCustomer(Customer customer) {
		customerRepository.save(customer);
		
	}

}
