package com.amigoscode.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> getCustomers() {
        var sql = """
                    SELECT * FROM customer ORDER BY id;
                """;
        //this we will extract it on it's own class--------------
        /*RowMapper<Customer> customerRowMapper = (rs, rowNum) -> {
            Customer customer = new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getInt("age")
            );

            return customer;
        };*/
        //----------------------------------------------------
        return jdbcTemplate.query(sql, customerRowMapper);



    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                    SELECT * FROM customer 
                    WHERE id = ?
                
                """;
//        List<Customer> customers = jdbcTemplate.query(sql,customerRowMapper,id);
//        Optional<Customer> customer = customers.stream().findFirst();

        return jdbcTemplate.query(sql,customerRowMapper,id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                    INSERT INTO customer (name,email,age) VALUES (?,?,?)
                """;
        int result = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());

        System.out.println("jdbcTemplate.update : "+result);// this means the number of rows inserted if we insert 10 customers the result will be 10 if insert 1 customer the result will be 1


    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                    SELECT count(id) FROM customer
                    WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,email);

       return count !=null && count>0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        var sql = """
                    DELETE FROM customer
                    WHERE id = ?
                
                """;
        int result = jdbcTemplate.update(sql, id);
        System.out.println("deleteCustomerById Result = "+result);
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        var sql = """
                    SELECT count(id) FROM customer
                    WHERE id= ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,id);
        return count != null && count >0;
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName() != null || customer.getEmail() != null || customer.getAge() != null){
            var sql = """
                    UPDATE customer
                    SET name= ? , email = ? , age= ?
                    WHERE id = ?
                """;

            jdbcTemplate.update(sql,customer.getName(),customer.getEmail(),customer.getAge(),customer.getId());
        }

        // Methode of Amigoscode----------------------------------------------------------------------

//        if (customer.getName() != null){
//            String sql = " UPDATE customer SET name= ? WHERE id = ? ";
//            int result = jdbcTemplate.update(sql, customer.getName(), customer.getId());
//            System.out.println("update customer name result = "+result);
//
//        }
//        if (customer.getAge() != null){
//            String sql = "UPDATE customer SET age= ? WHERE id = ? ";
//            int result = jdbcTemplate.update(sql, customer.getAge(), customer.getId());
//            System.out.println("update customer age result = "+result);
//
//        }
//        if (customer.getEmail() != null){
//            String sql = " UPDATE customer SET email= ? WHERE id = ? ";
//            int result = jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
//            System.out.println("update customer email result = "+result);
//
//        }
//
        //End Methode of Amigoscode----------------------------------------------------------


    }
}
