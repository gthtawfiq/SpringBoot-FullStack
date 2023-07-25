package com.amigoscode.customer;

import java.util.List;

import com.amigoscode.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/customers")
public class CustomerController {
	private final CustomerService customerService;
	private final JWTUtil jwtUtil;
	public CustomerController(CustomerService customerService,JWTUtil jwtUtil) {
		super();
		this.customerService = customerService;
		this.jwtUtil=jwtUtil;
	}
	
	@GetMapping()
	public List<CustomerDTO> getAllCustomers(){
		return customerService.getAllCustomers();
	}
	
	@GetMapping("{customerId}")
	public CustomerDTO getCustomerById(@PathVariable("customerId") Integer customerId) {
		return customerService.getCustomer(customerId);
	}
	
	/*@PostMapping()
	public void registerCustomer(@RequestBody CustomerRegistrationRequest Request) {
		customerService.addCustomer(Request);
		
	}*/
	@PostMapping()
	public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest Request) {
		customerService.addCustomer(Request);
		String jwtToken = jwtUtil.issueToken(Request.email(), "ROLE_USER");
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION,jwtToken)
				.build();

	}
	
	@DeleteMapping("{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Integer customerId) {
		customerService.deleteCustomer(customerId);
	}
	
	@PutMapping("{customerId}")
	public void updateCustomer(@PathVariable("customerId") Integer customerId,@RequestBody CustomerUpdateRequest updateRequest) {
		customerService.updateCustomer(updateRequest,customerId);
	}

//	@PutMapping("{customerId}")
//	public void updateCustomer(@PathVariable("customerId") Integer customerId,@RequestBody CustomerUpdateRequest updateRequest) {
//		customerService.updateCustomer(updateRequest,customerId);
//	}
	
	
	
}
