package com.opensource.Customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository("list")
public class CustomerListAccess implements CustomerDAO{

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer ezhil =  new Customer(
                1,
                "Ezhil",
                24,
                "ezhilhilarya@gmail.com"
        );
        customers.add(ezhil);
        Customer ajay =  new Customer(
                2,
                "Ajay",
                25,
                "ajay@gmail.com"
        );
        customers.add(ajay);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream().
                filter(c -> c.getId().equals(id))
                .findFirst();

    }

    @Override
    public void deleteCustomer(Integer id) {
        customers.stream().
                filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return  customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existPersonWithId(Integer id) {
        return customers.stream().anyMatch(c ->c.getId().equals(id));
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }
}
