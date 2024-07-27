package com.opensource.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existPersonWithEmail(String email);
    boolean existPersonWithId(Integer id);
    void deleteCustomer(Integer id);
    void updateCustomer(Customer update);
}
