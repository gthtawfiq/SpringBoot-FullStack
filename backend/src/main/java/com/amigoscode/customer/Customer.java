package com.amigoscode.customer;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "customer",
		uniqueConstraints = {
			@UniqueConstraint(
					name="email_unique",
					columnNames = "email"
			)
}
)
public class Customer {
	
	@Id
	@SequenceGenerator(
			name="customer_id_seq",
			sequenceName = "customer_id_seq",
			allocationSize = 1
	
			)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "customer_id_seq"
	)
	private Integer id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)//
	private String email;
	@Column(nullable = false)
	private Integer age;
	
	
	
	public Customer() {
		super();
	}



	public Customer(Integer id, String name, String email, Integer age) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.age = age;
	}
	public Customer(String name, String email, Integer age) {
		super();
		this.name = name;
		this.email = email;
		this.age = age;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public Integer getAge() {
		return age;
	}



	public void setAge(Integer age) {
		this.age = age;
	}



	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", age=" + age + "]";
	}



	@Override
	public int hashCode() {
		return Objects.hash(age, email, id, name);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(age, other.age) && Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name);
	}

}
