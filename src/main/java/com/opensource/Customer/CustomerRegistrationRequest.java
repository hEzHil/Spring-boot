package com.opensource.Customer;

public record CustomerRegistrationRequest(
        String email,
        Integer age,
        String name
) {
}
