package com.opensource.Customer;

import com.opensource.Exception.DuplicateResourceException;
import com.opensource.Exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("list") CustomerDAO customerDAO) {
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

    public void deleteCustomerById(Integer id){
        if(!customerDAO.existPersonWithId(id)){
            throw  new ResourceNotFound(
                    "customer with id  [%s] is not found".formatted(id)
            );
        }
        customerDAO.deleteCustomer(id);
    }
}
