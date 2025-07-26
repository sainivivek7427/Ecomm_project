package com.ecom.serviceimpl;

import com.ecom.entity.Customer;
import com.ecom.repository.CustomerRepository;
import com.ecom.service.EmailService;
import com.ecom.service.LoginRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class LoginRegServiceimpl implements LoginRegService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public Customer registerCustomer(Customer customer) {
        // Set UUID, roles, and time
        customer.setCuuid(UUID.randomUUID().toString());
        customer.setCreatedDate(System.currentTimeMillis());
        customer.setUpdateDate(System.currentTimeMillis());

        if ("ecomm_admin".equals(customer.getUsername())) {
            customer.setRole("Admin");
        } else {
            customer.setRole("User");
        }

        // Generate OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        customer.setOtp(otp);
        customer.setOtpGeneratedTime(System.currentTimeMillis());
        customer.setEnabled(false);

        // Save and send OTP
        Customer saved = customerRepository.save(customer);
        emailService.sendOtpEmail(customer.getEmail(), otp);
        return saved;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email); // ✅ correct
        return optionalCustomer.orElse(null);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
