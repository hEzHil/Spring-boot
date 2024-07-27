package com.opensource.Customer;

import com.opensource.Exception.DuplicateResourceException;
import com.opensource.Exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO) {
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

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){

        String email = customerRegistrationRequest.email();
        if(customerDAO.existPersonWithEmail(email)){
            throw new DuplicateResourceException(
                    "email already taken  "
            );
        }
        customerDAO.insertCustomer(
                new Customer(customerRegistrationRequest.name(),
                        customerRegistrationRequest.age(),
                        customerRegistrationRequest.email())
        );
    }
}
