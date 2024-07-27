package com.opensource.Customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/customers")
public class Controller {

    private final CustomerService customerService;

    public Controller(CustomerService customerService) {
        this.customerService = customerService;
    }
    /*
    Thi method is same as @GetMapping
     @RequestMapping(path = "api/v1/customers",method = RequestMethod.GET)
    */

    @GetMapping
    public List<Customer> getAllCustomer(){
        return customerService.getAllCustomer();
    }
    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable("id" )Integer id){
            return customerService.getCustomer(id);
    }
    @PostMapping
    public void registerCustomer(@RequestBody    CustomerRegistrationRequest request){
        customerService.addCustomer(request);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer id){
        customerService.deleteCustomerById(id);
    }
}
