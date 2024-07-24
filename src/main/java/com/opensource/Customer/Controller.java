package com.opensource.Customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class Controller {

    private final CustomerService customerService;

    public Controller(CustomerService customerService) {
        this.customerService = customerService;
    }
    /*
    Thi method is same as @GetMapping
     @RequestMapping(path = "api/v1/customers",method = RequestMethod.GET)
    */

    @GetMapping("api/v1/customers")
    public List<Customer> getAllCustomer(){
        return customerService.getAllCustomer();
    }
    @GetMapping("api/v1/customers/{id}")
    public Customer getCustomer(@PathVariable("id" )Integer id){
            return customerService.getCustomer(id);
    }

}
