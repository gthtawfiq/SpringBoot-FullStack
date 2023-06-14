package com.amigoscode;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.List;

@SpringBootApplication
public class SpringBootExampleApplication {
	
	//db for now
	/*private static List<Customer> customers;
	
	static {
		customers = new ArrayList<>();
		Customer alex = new Customer(1,"Alex","Alex@gmail.com",21);
		customers.add(alex);
		Customer jamila = new Customer(2,"jamila","jamila@gmail.com",19);
		customers.add(jamila);
		
	}*/

	public static void main(String[] args) {
		//ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootExampleApplication.class, args);
		
		 SpringApplication.run(SpringBootExampleApplication.class, args);

		 
		
		
		//String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
		
		/*for (String  beanDefinitionName : beanDefinitionNames) {
			System.out.println(beanDefinitionName);
			
		}*/
		
		
		
		
	}
	
	 @Bean
	 CommandLineRunner runner (CustomerRepository customerRepository) {
		 Faker faker = new Faker();
		 String name = faker.name().fullName();
		// String email = name+"@gmail.com";
		 String email = faker.internet().safeEmailAddress();

		 return args->{
			 Customer alex = new Customer(name,email,faker.number().numberBetween(18,60));
			//Customer jamila = new Customer("jamila","jamila@gmail.com",19);
			
			//List<Customer> customers = List.of(alex,jamila);
			 List<Customer> customers = List.of(alex);
			
			customerRepository.saveAll(customers);
		 };
	 }
	
	/*@GetMapping("/api/v1/customers")
	public List<Customer> getCustomers(){
		
		return customers;
	}
	
	@GetMapping("/api/v1/customer/{custumorId}")
	public Customer getCustomer(@PathVariable("custumorId") Integer custumorId){
		
		Customer cust = customers.stream().filter(c->c.getId().equals(custumorId))
		 					.findFirst()
		 					.orElseThrow(
		 							()-> new IllegalArgumentException("Customer with [%s] not found".formatted(custumorId)));
		return cust;
	}
	*/
	

		
		
		
		
		
		
	
	 

}
