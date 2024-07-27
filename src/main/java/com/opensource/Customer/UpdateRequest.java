package com.opensource.Customer;

public record UpdateRequest(
        String name,
        Integer age,
        String email) {
}
