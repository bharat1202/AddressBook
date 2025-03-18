package org.example.addressbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    // POST endpoint to test sending an email with input JSON
    @PostMapping("/api/test-email")
    public String testEmail(@RequestBody EmailTestRequestDTO request) {
        // Use provided email and username; if username is null, you might choose a default value
        String email = request.getEmail();
        String username = request.getUsername() != null ? request.getUsername() : "Test User";
        emailService.sendRegistrationEmail(email, username);
        return "This is a test email";
    }
}
