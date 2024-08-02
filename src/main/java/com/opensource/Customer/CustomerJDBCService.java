package com.opensource.Customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository("jdbc")
public class CustomerJDBCService implements CustomerDAO{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, age, email FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);


    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                SELECT id, name, age, email FROM customer WHERE id = ?
                """;

        return jdbcTemplate.query(sql,customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer (name, age, email) VALUES (?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getAge(),
                customer.getEmail());
        System.out.println("jdbcTemplate.update "+ result);

    }

    @Override
    public boolean existPersonWithEmail(String email) {
        var sql = """
                SELECT count(id) FROM customer WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);

        return count != null && count >0;
    }

    @Override
    public boolean existPersonWithId(Integer id) {
        var sql = """
                SELECT count(id) FROM customer WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count >0;
    }

    @Override
    public void deleteCustomer(Integer id) {
        var sql = """
                DELETE FROM customer WHERE id = ?
                """;
        int result =  jdbcTemplate.update(sql,id);
        System.out.println("Delete  "+ result);

    }

    @Override
    public void updateCustomer(Customer update) {
        if(update.getName() != null){
            String sql = "UPDATE customer SET name =? WHERE id = ?";
            int result = jdbcTemplate.update(sql,
                    update.getName(),
                    update.getId());
            System.out.println("Update customer name " + result);
        }
        if(update.getAge() != null){
            String sql = "UPDATE customer SET age =? WHERE id = ?";
            int result = jdbcTemplate.update(sql,
                    update.getAge(),
                    update.getId());
            System.out.println("Update customer age " + result);
        }
        if(update.getEmail() != null){
            String sql = "UPDATE customer SET email =? WHERE id = ?";
            int result = jdbcTemplate.update(sql,
                     update.getEmail(),
                     update.getId());
            System.out.println("Update customer email " + result);
        }

    }
}
