package org.example.addressbook;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordResetService passwordResetService;

    // Registration Endpoint
    @PostMapping("/register")
    public ResponseDTO<String> register(@Valid @RequestBody AuthRequestDTO request) {
        return authService.register(request);
    }

    // Forgot Password Endpoint
    @PostMapping("/forgot-password")
    public ResponseDTO<String> forgotPassword(@RequestBody PasswordResetRequestDTO request) {
        passwordResetService.generateResetToken(request.getEmail());
        return new ResponseDTO<>("Password reset link sent to email.", null);
    }

    // Additional endpoints (e.g., login, reset-password) can be added here.
}
