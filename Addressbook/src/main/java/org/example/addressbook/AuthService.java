package org.example.addressbook;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;  // Inject the EmailService

    // Register New User using AuthRequestDTO
    public ResponseDTO<String> register(AuthRequestDTO request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return new ResponseDTO<>("Username already taken.", null);
        }

        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(hashedPassword);
        user.setEmail(request.getEmail());  // Set email from request
        userRepository.save(user);

        // Send registration email
        emailService.sendRegistrationEmail(user.getEmail(), user.getUsername());

        return new ResponseDTO<>("User registered successfully.", null);
    }

    // Login User using AuthRequestDTO
    public ResponseDTO<String> login(AuthRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtUtil.generateToken(user.getUsername());
            return new ResponseDTO<>("Login successful.", token);

        } catch (Exception e) {
            return new ResponseDTO<>("Invalid credentials.", null);
        }
    }
}
