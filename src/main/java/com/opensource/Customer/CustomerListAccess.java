package com.opensource.Customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
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
}
