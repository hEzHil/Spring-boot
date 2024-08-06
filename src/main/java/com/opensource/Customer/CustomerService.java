package com.opensource.Customer;

import com.opensource.Exception.DuplicateResourceException;
import com.opensource.Exception.RequestValidationException;
import com.opensource.Exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
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
                    "email already taken"
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

    public void updateCustomer(Integer id, UpdateRequest updateRequest){
        Customer customer = getCustomer(id);
        boolean changes = false;

        if(updateRequest.name() != null && !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());
            changes = true;
        }
        if(updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())){
            customer.setAge(updateRequest.age());
            changes = true;
        }
        if(updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())){
            if(customerDAO.existPersonWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }
        if(!changes){
            throw new RequestValidationException("No data changes");
        }
        customerDAO.updateCustomer(customer);
    }
}
