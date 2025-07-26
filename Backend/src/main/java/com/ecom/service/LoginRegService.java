package com.ecom.service;

import com.ecom.entity.Customer;

public interface LoginRegService {
    Customer registerCustomer(Customer customer);

    Customer getCustomerByEmail(String email);

    Customer saveCustomer(Customer customer);
}
