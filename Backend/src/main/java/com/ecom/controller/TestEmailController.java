package com.ecom.controller;

import com.ecom.service.EmailService;
import com.ecom.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class TestEmailController {

    @Autowired
    private EmailService emailService;

    // Endpoint to send OTP email
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email, @RequestParam String otp) {
        emailService.sendOtpEmail(email, otp);
        return "OTP sent successfully to " + email;
    }
}