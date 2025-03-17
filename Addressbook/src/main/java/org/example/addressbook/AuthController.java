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

    @PostMapping("/register")
    public ResponseDTO<String> register(@Valid @RequestBody AuthRequestDTO request) {
        try {
            authService.register(request);
            return new ResponseDTO<>("User registered successfully.", null);
        } catch (Exception e) {
            return new ResponseDTO<>("Registration failed: " + e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public ResponseDTO<String> login(@Valid @RequestBody AuthRequestDTO request) {
        try {
            ResponseDTO<String> response = authService.login(request);
            return response;
        } catch (Exception e) {
            return new ResponseDTO<>("Login failed: " + e.getMessage(), null);
        }
    }
}
