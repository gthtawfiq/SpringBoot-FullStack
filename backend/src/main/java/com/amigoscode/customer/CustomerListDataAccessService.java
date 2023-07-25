package com.amigoscode.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
	
	private static final List<Customer> customers;
	
	static {
		customers = new ArrayList<>();
		Customer alex = new Customer(1,"Alex","Alex@gmail.com", "password", 21, Gender.MALE);
		customers.add(alex);
		Customer jamila = new Customer(2,"jamila","jamila@gmail.com", "password", 19, Gender.MALE);
		customers.add(jamila);
	}

	@Override
	public List<Customer> getCustomers() {
		// TODO Auto-generated method stub
		return customers;
	}

	@Override
	public Optional<Customer> selectCustomerById(Integer id) {
		// TODO Auto-generated method stub
		return customers.stream().filter(c->c.getId().equals(id))
					.findFirst();
							
	}

	@Override
	public void insertCustomer(Customer customer) {
		customers.add(customer);
		
	}

	@Override
	public boolean existsPersonWithEmail(String email) {
		// TODO Auto-generated method stub
		return customers.stream().anyMatch(c->c.getEmail().equals(email));
	}

	@Override
	public void deleteCustomerById(Integer id) {
		// TODO Auto-generated method stub
		customers.stream().filter(c->c.getId().equals(id)).findFirst().ifPresent(o->customers.remove(o));
		
	}

	@Override
	public boolean existsPersonWithId(Integer id) {
		// TODO Auto-generated method stub
		return customers.stream().anyMatch(c->c.getId().equals(id));
	}

	@Override
	public void updateCustomer(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Customer> selectUserByEmail(String email) {
		return customers.stream().filter(c->c.getUsername().equals(email))
				.findFirst();
	}

}
