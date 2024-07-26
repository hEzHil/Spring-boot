package com.opensource.Customer;

import com.opensource.Exception.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    List<Customer> getAllCustomer(){
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDAO.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        "customer with id  [%s] is not found".formatted(id)));

    }
}
