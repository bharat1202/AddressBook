package org.example.addressbook;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil; // For generating JWT tokens (if needed for login)

    @Autowired
    private EmailService emailService;

    // Register New User using AuthRequestDTO and send a registration email.
    public ResponseDTO<String> register(AuthRequestDTO request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return new ResponseDTO<>("Username already taken.", null);
        }

        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        // Send registration email
        emailService.sendRegistrationEmail(user.getEmail(), user.getUsername());

        return new ResponseDTO<>("User registered successfully.", null);
    }

    // (Optional) Implement login if needed...
}
