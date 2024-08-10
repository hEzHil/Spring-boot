package com.opensource.Customer;

public record CustomerRegistrationRequest(
        String name,
        Integer age,
        String email
) {
}
